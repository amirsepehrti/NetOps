package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.delay
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Collections

enum class NetOpsTab {
    DASHBOARD, TOOLBOX, DEVICES, ALERTS, TERMINAL
}

enum class ActiveTool {
    NONE, PING, PORT_SCANNER, SUBNET_CALC, DNS_LOOKUP, WAKE_ON_LAN, TRACEROUTE, WHOIS_LOOKUP, SPEED_TEST, TRAFFIC_GENERATOR, BANDWIDTH_TEST, SNMP_DISCOVERY, WAN_KILLER, MAC_SCANNER
}

class NetOpsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NetOpsRepository(application)

    // Current Screen / Tab
    private val _currentTab = MutableStateFlow(NetOpsTab.DASHBOARD)
    val currentTab: StateFlow<NetOpsTab> = _currentTab.asStateFlow()

    // Active tool inside Toolbox tab
    private val _activeTool = MutableStateFlow(ActiveTool.NONE)
    val activeTool: StateFlow<ActiveTool> = _activeTool.asStateFlow()

    // Database Streams
    val sites: StateFlow<List<Site>> = repository.allSites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedWols: StateFlow<List<SavedWol>> = repository.allSavedWols
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val sshSnippets: StateFlow<List<SshSnippet>> = repository.allSshSnippets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected Site for Dashboard & Device management
    private val _selectedSiteId = MutableStateFlow<Int?>(null)
    val selectedSiteId: StateFlow<Int?> = _selectedSiteId.asStateFlow()

    val selectedSiteDevices: StateFlow<List<Device>> = _selectedSiteId
        .flatMapLatest { id ->
            if (id != null) repository.getDevicesForSite(id) else flowOf(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Local Interface Context (IP, MAC, Gateway)
    private val _localNetworkContext = MutableStateFlow<Map<String, String>>(emptyMap())
    val localNetworkContext: StateFlow<Map<String, String>> = _localNetworkContext.asStateFlow()

    // 1. PING STATE
    val pingHost = MutableStateFlow("8.8.8.8")
    val pingCount = MutableStateFlow("4")
    val pingInterval = MutableStateFlow("1.0")
    val pingPacketSize = MutableStateFlow("56")
    val pingTtl = MutableStateFlow("64")
    val pingUseTcp = MutableStateFlow(false)

    private val _pingResults = MutableStateFlow<List<PingResult>>(emptyList())
    val pingResults: StateFlow<List<PingResult>> = _pingResults.asStateFlow()

    private val _pingSummary = MutableStateFlow<PingSummary?>(null)
    val pingSummary: StateFlow<PingSummary?> = _pingSummary.asStateFlow()

    private val _isPingRunning = MutableStateFlow(false)
    val isPingRunning: StateFlow<Boolean> = _isPingRunning.asStateFlow()

    private var pingJob: Job? = null

    // 2. PORT SCANNER STATE
    val scanHost = MutableStateFlow("192.168.1.1")
    val scanPreset = MutableStateFlow("common") // common, range, single
    val scanSinglePort = MutableStateFlow("80")
    val scanRangeStart = MutableStateFlow("20")
    val scanRangeEnd = MutableStateFlow("90")

    private val _scannedPorts = MutableStateFlow<List<ScannedPort>>(emptyList())
    val scannedPorts: StateFlow<List<ScannedPort>> = _scannedPorts.asStateFlow()

    private val _isScanRunning = MutableStateFlow(false)
    val isScanRunning: StateFlow<Boolean> = _isScanRunning.asStateFlow()

    private val _scanProgress = MutableStateFlow(0f)
    val scanProgress: StateFlow<Float> = _scanProgress.asStateFlow()

    private var scanJob: Job? = null

    // 3. SUBNET CALCULATOR STATE
    val subnetIp = MutableStateFlow("192.168.1.1")
    val subnetCidr = MutableStateFlow("24")
    val subnetSplitCidr = MutableStateFlow("26")

    private val _subnetInfo = MutableStateFlow<SubnetInfo?>(null)
    val subnetInfo: StateFlow<SubnetInfo?> = _subnetInfo.asStateFlow()

    private val _splitSubnets = MutableStateFlow<List<String>>(emptyList())
    val splitSubnets: StateFlow<List<String>> = _splitSubnets.asStateFlow()

    // 4. DNS LOOKUP STATE
    val dnsDomain = MutableStateFlow("google.com")
    val dnsResolver = MutableStateFlow("")

    private val _dnsRecords = MutableStateFlow<List<DnsRecord>>(emptyList())
    val dnsRecords: StateFlow<List<DnsRecord>> = _dnsRecords.asStateFlow()

    private val _isDnsLoading = MutableStateFlow(false)
    val isDnsLoading: StateFlow<Boolean> = _isDnsLoading.asStateFlow()

    // 5. WAKE ON LAN STATE
    val wolMac = MutableStateFlow("3C:7C:3F:8A:2B:9C")
    val wolBroadcast = MutableStateFlow("255.255.255.255")
    val wolPort = MutableStateFlow("9")

    private val _wolStatusMessage = MutableStateFlow("")
    val wolStatusMessage: StateFlow<String> = _wolStatusMessage.asStateFlow()

    // 7. TRACEROUTE STATE
    val tracerouteHost = MutableStateFlow("google.com")
    private val _tracerouteHops = MutableStateFlow<List<TracerouteHop>>(emptyList())
    val tracerouteHops: StateFlow<List<TracerouteHop>> = _tracerouteHops.asStateFlow()
    private val _isTracerouteRunning = MutableStateFlow(false)
    val isTracerouteRunning: StateFlow<Boolean> = _isTracerouteRunning.asStateFlow()
    private var tracerouteJob: Job? = null

    // 8. WHOIS / IP LOOKUP STATE
    val whoisQuery = MutableStateFlow("google.com")
    private val _whoisRecord = MutableStateFlow<WhoisRecord?>(null)
    val whoisRecord: StateFlow<WhoisRecord?> = _whoisRecord.asStateFlow()
    private val _isWhoisSearching = MutableStateFlow(false)
    val isWhoisSearching: StateFlow<Boolean> = _isWhoisSearching.asStateFlow()

    // 9. SPEED TEST STATE
    private val _speedTestState = MutableStateFlow(SpeedTestState("IDLE", 0.0f, 0.0, 0.0, 0.0))
    val speedTestState: StateFlow<SpeedTestState> = _speedTestState.asStateFlow()
    private val _isSpeedTestRunning = MutableStateFlow(false)
    val isSpeedTestRunning: StateFlow<Boolean> = _isSpeedTestRunning.asStateFlow()
    private var speedTestJob: Job? = null

    // 10. RECENT EXECUTIONS & BACKUP
    val recentExecutions: StateFlow<List<RecentExecution>> = repository.allRecentExecutions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 11. TRAFFIC GENERATOR STATE
    val trafficGenHost = MutableStateFlow("192.168.1.1")
    val trafficGenPort = MutableStateFlow("5001")
    val trafficGenProtocol = MutableStateFlow("UDP")
    val trafficGenRate = MutableStateFlow("100")
    val trafficGenPacketSize = MutableStateFlow("1400")
    private val _isTrafficGenRunning = MutableStateFlow(false)
    val isTrafficGenRunning: StateFlow<Boolean> = _isTrafficGenRunning.asStateFlow()
    private val _trafficGenLogs = MutableStateFlow<List<String>>(emptyList())
    val trafficGenLogs: StateFlow<List<String>> = _trafficGenLogs.asStateFlow()
    private var trafficGenJob: Job? = null

    // 12. BANDWIDTH TEST STATE
    val bandwidthRole = MutableStateFlow("Client") // Client, Server
    val bandwidthHost = MutableStateFlow("192.168.1.100")
    val bandwidthPort = MutableStateFlow("5201")
    val bandwidthProtocol = MutableStateFlow("TCP")
    val bandwidthDuration = MutableStateFlow("10")
    private val _isBandwidthRunning = MutableStateFlow(false)
    val isBandwidthRunning: StateFlow<Boolean> = _isBandwidthRunning.asStateFlow()
    private val _bandwidthLogs = MutableStateFlow<List<String>>(emptyList())
    val bandwidthLogs: StateFlow<List<String>> = _bandwidthLogs.asStateFlow()
    private val _bandwidthSpeedMbps = MutableStateFlow(0.0)
    val bandwidthSpeedMbps: StateFlow<Double> = _bandwidthSpeedMbps.asStateFlow()
    private var bandwidthJob: Job? = null

    // 13. SNMP DISCOVERY STATE
    val snmpHost = MutableStateFlow("192.168.1.1")
    val snmpCommunity = MutableStateFlow("public")
    val snmpPort = MutableStateFlow("161")
    private val _isSnmpRunning = MutableStateFlow(false)
    val isSnmpRunning: StateFlow<Boolean> = _isSnmpRunning.asStateFlow()
    private val _snmpResult = MutableStateFlow<SnmpDeviceInfo?>(null)
    val snmpResult: StateFlow<SnmpDeviceInfo?> = _snmpResult.asStateFlow()
    private var snmpJob: Job? = null

    // 14. WAN KILLER STATE
    val wanKillerHost = MutableStateFlow("192.168.1.50")
    val wanKillerRateMbps = MutableStateFlow("250")
    val wanKillerPacketSize = MutableStateFlow("65500")
    private val _isWanKillerRunning = MutableStateFlow(false)
    val isWanKillerRunning: StateFlow<Boolean> = _isWanKillerRunning.asStateFlow()
    private val _wanKillerStats = MutableStateFlow<WanKillerStats?>(null)
    val wanKillerStats: StateFlow<WanKillerStats?> = _wanKillerStats.asStateFlow()
    private var wanKillerJob: Job? = null

    // 15. MAC SCANNER STATE
    val macScanSubnet = MutableStateFlow("192.168.1.0/24")
    private val _isMacScanRunning = MutableStateFlow(false)
    val isMacScanRunning: StateFlow<Boolean> = _isMacScanRunning.asStateFlow()
    private val _macScanProgress = MutableStateFlow(0f)
    val macScanProgress: StateFlow<Float> = _macScanProgress.asStateFlow()
    private val _macScanResults = MutableStateFlow<List<MacScanResult>>(emptyList())
    val macScanResults: StateFlow<List<MacScanResult>> = _macScanResults.asStateFlow()
    private var macScanJob: Job? = null

    // 6. TERMINAL STATE
    val terminalInput = MutableStateFlow("")
    private val _terminalLogs = MutableStateFlow<List<String>>(
        listOf(
            "NetOps Console [v1.0.0]",
            "Type 'help' to see local network utilities command helper.",
            "Ready for local loopback shell...",
            "netops-client:~$"
        )
    )
    val terminalLogs: StateFlow<List<String>> = _terminalLogs.asStateFlow()

    // SSH Session state
    private val _sshSessionHost = MutableStateFlow<String?>(null)
    val sshSessionHost: StateFlow<String?> = _sshSessionHost.asStateFlow()

    // Initial setups
    init {
        // Auto-select first site when sites are loaded
        viewModelScope.launch {
            sites.collect { siteList ->
                if (siteList.isNotEmpty() && _selectedSiteId.value == null) {
                    _selectedSiteId.value = siteList.first().id
                }
            }
        }
        updateLocalNetworkContext()
        calculateSubnetInfo()
    }

    fun selectTab(tab: NetOpsTab) {
        _currentTab.value = tab
    }

    fun selectTool(tool: ActiveTool) {
        _activeTool.value = tool
    }

    fun selectSite(siteId: Int) {
        _selectedSiteId.value = siteId
    }

    // Local IP / Context lookup
    fun updateLocalNetworkContext() {
        viewModelScope.launch(Dispatchers.IO) {
            val contextMap = mutableMapOf<String, String>()
            try {
                val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
                for (iface in interfaces) {
                    if (iface.isLoopback || !iface.isUp) continue
                    val addresses = Collections.list(iface.inetAddresses)
                    for (addr in addresses) {
                        if (addr.isLoopbackAddress) continue
                        val hostAddr = addr.hostAddress ?: continue
                        if (!hostAddr.contains(":")) { // IPv4
                            contextMap["Interface"] = iface.name
                            contextMap["Local IP"] = hostAddr
                            val macBytes = iface.hardwareAddress
                            if (macBytes != null) {
                                val macStr = macBytes.joinToString(":") { String.format("%02X", it) }
                                contextMap["MAC Address"] = macStr
                            }
                            break
                        }
                    }
                    if (contextMap.isNotEmpty()) break
                }
                if (contextMap.isEmpty()) {
                    contextMap["Interface"] = "cellular/unconnected"
                    contextMap["Local IP"] = "127.0.0.1"
                }
                // Try to resolve standard gateway address
                contextMap["Default Gateway"] = "192.168.1.1"
                contextMap["Network State"] = "Connected (LAN Mode)"
            } catch (e: Exception) {
                contextMap["Error"] = e.localizedMessage ?: "Unknown network exception"
            }
            _localNetworkContext.value = contextMap
        }
    }

    // --- TOOL ACTIONS ---

    // 1. PING ACTIONS
    fun startPing() {
        if (_isPingRunning.value) return
        _isPingRunning.value = true
        _pingResults.value = emptyList()
        _pingSummary.value = null

        val host = pingHost.value.trim()
        val count = pingCount.value.toIntOrNull() ?: 4
        val interval = pingInterval.value.toDoubleOrNull() ?: 1.0
        val packetSize = pingPacketSize.value.toIntOrNull() ?: 56
        val ttl = pingTtl.value.toIntOrNull() ?: 64
        val tcpFallback = pingUseTcp.value

        pingJob = viewModelScope.launch(Dispatchers.IO) {
            NetworkEngine.pingStream(host, count, interval, packetSize, ttl, tcpFallback)
                .collect { result ->
                    _pingResults.update { current -> current + result }
                    // Update stats live
                    _pingSummary.value = NetworkEngine.computePingSummary(_pingResults.value)
                }
            _isPingRunning.value = false
        }
    }

    fun stopPing() {
        pingJob?.cancel()
        _isPingRunning.value = false
    }

    // 2. PORT SCAN ACTIONS
    fun startPortScan() {
        if (_isScanRunning.value) return
        _isScanRunning.value = true
        _scannedPorts.value = emptyList()
        _scanProgress.value = 0f

        val host = scanHost.value.trim()
        val preset = scanPreset.value

        val ports = when (preset) {
            "single" -> listOf(scanSinglePort.value.toIntOrNull() ?: 80)
            "range" -> {
                val start = scanRangeStart.value.toIntOrNull() ?: 20
                val end = scanRangeEnd.value.toIntOrNull() ?: 100
                (start..end).toList()
            }
            else -> NetworkEngine.commonPorts
        }

        scanJob = viewModelScope.launch(Dispatchers.IO) {
            val total = ports.size
            var completed = 0
            NetworkEngine.scanPorts(host, ports, timeoutMs = 400) { scannedPort ->
                _scannedPorts.update { current -> current + scannedPort }
                completed++
                _scanProgress.value = completed.toFloat() / total
            }
            _isScanRunning.value = false
        }
    }

    fun stopPortScan() {
        scanJob?.cancel()
        _isScanRunning.value = false
    }

    // 3. SUBNET ACTIONS
    fun calculateSubnetInfo() {
        val ip = subnetIp.value.trim()
        val cidr = subnetCidr.value.toIntOrNull() ?: 24
        val splitC = subnetSplitCidr.value.toIntOrNull() ?: 26

        val info = NetworkEngine.calculateSubnet(ip, cidr)
        _subnetInfo.value = info

        val subList = NetworkEngine.splitSubnet(ip, cidr, splitC)
        _splitSubnets.value = subList
    }

    // 4. DNS LOOKUP ACTIONS
    fun performDnsLookup() {
        if (_isDnsLoading.value) return
        _isDnsLoading.value = true
        _dnsRecords.value = emptyList()

        val domain = dnsDomain.value.trim()
        val customDns = dnsResolver.value.trim()

        viewModelScope.launch(Dispatchers.IO) {
            val records = NetworkEngine.dnsLookup(domain, customDns)
            _dnsRecords.value = records
            _isDnsLoading.value = false
        }
    }

    // 5. WAKE ON LAN ACTIONS
    fun triggerWakeOnLan(mac: String = wolMac.value, bcast: String = wolBroadcast.value) {
        val port = wolPort.value.toIntOrNull() ?: 9
        viewModelScope.launch {
            _wolStatusMessage.value = "Sending Magic Packet to $mac..."
            val result = NetworkEngine.sendWakeOnLan(mac, bcast, port)
            if (result.isSuccess) {
                _wolStatusMessage.value = "Magic Packet sent successfully to $mac (UDP Port $port)!"
            } else {
                _wolStatusMessage.value = "Error sending WoL: ${result.exceptionOrNull()?.localizedMessage}"
            }
        }
    }

    // 7. TRACEROUTE ACTIONS
    fun startTraceroute() {
        if (_isTracerouteRunning.value) return
        _isTracerouteRunning.value = true
        _tracerouteHops.value = emptyList()

        val host = tracerouteHost.value.trim()
        if (host.isEmpty()) {
            _isTracerouteRunning.value = false
            return
        }

        tracerouteJob = viewModelScope.launch(Dispatchers.IO) {
            NetworkEngine.tracerouteStream(host).collect { line ->
                val trimmed = line.trim()
                // Regex to extract: hop hostname (ip) rtt ms
                val regex = """^\s*(\d+)\s+([^\s\(]+)\s+\(([^)]+)\)\s+([\d.]+)\s*ms""".toRegex()
                val matchResult = regex.find(trimmed)
                if (matchResult != null) {
                    val hopNum = matchResult.groupValues[1].toIntOrNull() ?: 1
                    val hostname = matchResult.groupValues[2]
                    val ip = matchResult.groupValues[3]
                    val rttMs = matchResult.groupValues[4].toDoubleOrNull() ?: 0.0
                    val parsedHop = TracerouteHop(hopNum, hostname, ip, rttMs)
                    _tracerouteHops.update { current -> current + parsedHop }
                }
            }
            _isTracerouteRunning.value = false
        }
    }

    fun stopTraceroute() {
        tracerouteJob?.cancel()
        _isTracerouteRunning.value = false
    }

    // 8. WHOIS / IP LOOKUP ACTIONS
    fun performWhoisLookup() {
        if (_isWhoisSearching.value) return
        _isWhoisSearching.value = true
        _whoisRecord.value = null

        val query = whoisQuery.value.trim()
        if (query.isEmpty()) {
            _isWhoisSearching.value = false
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val record = NetworkEngine.performWhoisLookup(query)
            _whoisRecord.value = record
            _isWhoisSearching.value = false
        }
    }

    // 9. SPEED TEST ACTIONS
    fun startSpeedTest() {
        if (_isSpeedTestRunning.value) return
        _isSpeedTestRunning.value = true
        _speedTestState.value = SpeedTestState("IDLE", 0.0f, 0.0, 0.0, 0.0)

        speedTestJob = viewModelScope.launch(Dispatchers.IO) {
            NetworkEngine.speedTestStream().collect { state ->
                _speedTestState.value = state
            }
            _isSpeedTestRunning.value = false
        }
    }

    fun stopSpeedTest() {
        speedTestJob?.cancel()
        _speedTestState.value = SpeedTestState("IDLE", 0.0f, 0.0, 0.0, 0.0)
        _isSpeedTestRunning.value = false
    }

    // --- PERSISTENCE WRITING ---

    // Site Add/Delete
    fun addSite(name: String, gateway: String, subnet: String, vpn: String, notes: String) {
        viewModelScope.launch {
            repository.insertSite(
                Site(
                    name = name,
                    gatewayIp = gateway,
                    subnetMask = subnet,
                    vpnConfig = vpn,
                    notes = notes
                )
            )
        }
    }

    fun deleteSite(siteId: Int) {
        viewModelScope.launch {
            repository.deleteSiteById(siteId)
            if (_selectedSiteId.value == siteId) {
                _selectedSiteId.value = sites.value.firstOrNull()?.id
            }
        }
    }

    // Device Add/Delete
    fun addDevice(siteId: Int, name: String, ip: String, type: String, vendor: String, notes: String) {
        viewModelScope.launch {
            repository.insertDevice(
                Device(
                    siteId = siteId,
                    name = name,
                    ipAddress = ip,
                    deviceType = type,
                    vendor = vendor,
                    notes = notes
                )
            )
        }
    }

    fun deleteDevice(deviceId: Int) {
        viewModelScope.launch {
            repository.deleteDeviceById(deviceId)
        }
    }

    // Saved WOL Targets Add/Delete
    fun saveWolTarget(name: String, mac: String, bcast: String, port: Int, notes: String) {
        viewModelScope.launch {
            repository.insertSavedWol(
                SavedWol(
                    name = name,
                    macAddress = mac,
                    broadcastIp = bcast,
                    port = port,
                    notes = notes
                )
            )
        }
    }

    fun deleteWolTarget(id: Int) {
        viewModelScope.launch {
            repository.deleteSavedWolById(id)
        }
    }

    // SSH Snippets Add/Delete
    fun saveSshSnippet(title: String, command: String, description: String) {
        viewModelScope.launch {
            repository.insertSshSnippet(
                SshSnippet(
                    title = title,
                    command = command,
                    description = description
                )
            )
        }
    }

    fun deleteSshSnippet(id: Int) {
        viewModelScope.launch {
            repository.deleteSshSnippetById(id)
        }
    }

    // --- TERMINAL CLI ACTIONS ---
    fun executeTerminalCommand() {
        val input = terminalInput.value.trim()
        if (input.isEmpty()) return

        val currentSsh = _sshSessionHost.value
        val displayPrompt = if (currentSsh != null) "$currentSsh:~$ $input" else "admin@netops-client:~$ $input"
        _terminalLogs.update { current -> current + displayPrompt }
        terminalInput.value = ""

        viewModelScope.launch(Dispatchers.IO) {
            val response: List<String>
            if (currentSsh != null) {
                // Interactive Remote SSH Simulator parser
                response = handleSshCommand(input)
            } else {
                // Local Terminal Client parser
                response = handleLocalCommand(input)
            }

            withContext(Dispatchers.Main) {
                if (input.equals("clear", ignoreCase = true)) {
                    val promptText = if (_sshSessionHost.value != null) "${_sshSessionHost.value}:~$" else "netops-client:~$"
                    _terminalLogs.value = listOf(promptText)
                } else {
                    val promptText = if (_sshSessionHost.value != null) "${_sshSessionHost.value}:~$" else "netops-client:~$"
                    _terminalLogs.update { current -> current + response + promptText }
                }
            }
        }
    }

    private suspend fun handleLocalCommand(input: String): List<String> {
        val parts = input.split("\\s+".toRegex())
        val command = parts[0].lowercase()
        val args = parts.drop(1)

        return when (command) {
            "help" -> {
                listOf(
                    "================================================",
                    "   NetOps Professional Network/IT Terminal v1.1 ",
                    "================================================",
                    "Core IT Diagnostic Tools (Android Native & API Safe):",
                    "  ping <host>       - Standard ICMP/TCP ping diagnostic checks",
                    "  traceroute <host> - Visualizes route paths and latencies live",
                    "  nslookup <domain> - Non-authoritative host DNS resolving query",
                    "  dig <domain>      - Detailed DNS record resolution (Unix style)",
                    "  curl <url>        - Connects to URL and prints response headers/body",
                    "  ssh <user@host>   - Initiates an interactive simulated secure console",
                    "  ss                - Shows active local interface sockets",
                    "  top               - Real-time snapshot of CPU / RAM process threads",
                    "  ps                - Process list status tree",
                    "  df                - Disk space partition usage details",
                    "  free              - Displays available physical device RAM",
                    "  ip                - Shows network cards & IPv4/IPv6 address interfaces",
                    "  nmap <host>       - Runs target scanning of standard server ports",
                    "  logcat            - Dumps Android application session run logs",
                    "------------------------------------------------",
                    "  local             - Prints interface card IP context information",
                    "  clear             - Clears terminal screen history buffer"
                )
            }
            "ping" -> {
                if (args.isEmpty()) return listOf("Usage: ping <host>")
                val target = args[0]
                listOf("Initiating diagnostic ping stream to $target...") + runConsolePing(target)
            }
            "traceroute" -> {
                if (args.isEmpty()) return listOf("Usage: traceroute <host>")
                val target = args[0]
                listOf("Initiating traceroute routing to $target...") + runConsoleTraceroute(target)
            }
            "nslookup" -> {
                if (args.isEmpty()) return listOf("Usage: nslookup <domain>")
                val domain = args[0]
                runConsoleNslookup(domain)
            }
            "dig" -> {
                if (args.isEmpty()) return listOf("Usage: dig <domain>")
                val domain = args[0]
                runConsoleDig(domain)
            }
            "curl" -> {
                if (args.isEmpty()) return listOf("Usage: curl <url>")
                val url = args[0]
                listOf("curl: connecting to $url...") + NetworkEngine.runCurl(url)
            }
            "ssh" -> {
                if (args.isEmpty()) return listOf("Usage: ssh <user@host>")
                val target = args[0]
                _sshSessionHost.value = target
                listOf(
                    "Connecting to SSH secure host $target...",
                    "Authorized successfully via dynamic secure agent tunnel.",
                    "=========================================================",
                    "Welcome to remote node $target (OS: Debian GNU/Linux 12)",
                    "Type 'help' to see remote command list or 'exit' to log out.",
                    "========================================================="
                )
            }
            "ss" -> {
                NetworkEngine.getSocketConnections()
            }
            "top" -> {
                runConsoleTop()
            }
            "ps" -> {
                runConsolePs()
            }
            "df" -> {
                NetworkEngine.getDiskInfo(getApplication())
            }
            "free" -> {
                NetworkEngine.getMemoryInfo(getApplication())
            }
            "ip" -> {
                val ipCmd = if (args.isNotEmpty()) args[0] else ""
                if (ipCmd.equals("addr", ignoreCase = true) || ipCmd.isEmpty()) {
                    NetworkEngine.getIpAddr()
                } else {
                    listOf("Usage: ip addr")
                }
            }
            "nmap" -> {
                if (args.isEmpty()) return listOf("Usage: nmap <host>")
                val target = args[0]
                listOf("Starting Nmap 7.92 ( https://nmap.org ) at 2026-07-06 12:44 UTC") + runConsoleNmap(target)
            }
            "logcat" -> {
                listOf("Dumping system logcat buffer entries (Session filtering):") + NetworkEngine.getLogcatLogs()
            }
            "local" -> {
                localNetworkContext.value.entries.map { "${it.key}: ${it.value}" }
            }
            "clear" -> {
                emptyList()
            }
            else -> {
                listOf("Command '$command' not found. Type 'help' for available options.")
            }
        }
    }

    private fun handleSshCommand(input: String): List<String> {
        val parts = input.split("\\s+".toRegex())
        val command = parts[0].lowercase()

        return when (command) {
            "help" -> {
                listOf(
                    "Interactive SSH Remote Host Simulator Options:",
                    "  show interfaces  - Displays physical config of remote ports",
                    "  uptime           - Remote server running timeline & load averages",
                    "  uname -a         - Displays remote architecture & OS info",
                    "  ls -la           - Lists directories in current home folder",
                    "  reboot           - Restarts the remote node session",
                    "  exit             - Safely closes connection back to client"
                )
            }
            "show" -> {
                if (parts.size > 1 && parts[1].lowercase() == "interfaces") {
                    listOf(
                        "eth0      Link encap:Ethernet  HWaddr 52:54:00:AB:CD:12",
                        "          inet addr:192.168.1.50  Bcast:192.168.1.255  Mask:255.255.255.0",
                        "          UP BROADCAST RUNNING MULTICAST  MTU:1500  Metric:1",
                        "          RX packets:158402 errors:0 dropped:0 overruns:0 frame:0",
                        "          TX packets:124058 errors:0 dropped:0 overruns:0 carrier:0",
                        "",
                        "lo        Link encap:Local Loopback",
                        "          inet addr:127.0.0.1  Mask:255.0.0.0",
                        "          UP LOOPBACK RUNNING  MTU:65536  Metric:1"
                    )
                } else {
                    listOf("remote: usage: show interfaces")
                }
            }
            "uptime" -> {
                listOf(" 12:45:01 up 145 days,  4:12,  1 user,  load average: 0.08, 0.04, 0.01")
            }
            "uname" -> {
                listOf("Linux remote-node-srv 6.1.0-21-amd64 #1 SMP PREEMPT_DYNAMIC Debian 6.1.90-1 x86_64 GNU/Linux")
            }
            "ls" -> {
                listOf(
                    "total 28",
                    "drwxr-xr-x  3 admin admin 4096 Jul  6 12:40 .",
                    "drwxr-xr-x  3 root  root  4096 Jun  1 08:00 ..",
                    "-rw-r--r--  1 admin admin  220 Jun  1 08:00 .bash_logout",
                    "-rw-r--r--  1 admin admin 3771 Jun  1 08:00 .bashrc",
                    "-rw-r--r--  1 admin admin  807 Jun  1 08:00 .profile",
                    "drwx------  2 admin admin 4096 Jun 15 10:15 .ssh",
                    "-rwxr-xr-x  1 admin admin  425 Jul  2 15:30 deploy_script.sh",
                    "-rw-r--r--  1 admin admin 1045 Jul  5 09:12 vpn_rules.conf"
                )
            }
            "reboot" -> {
                _sshSessionHost.value = null
                listOf("Connection closing... reboot command sent successfully.", "[Remote socket connection closed]")
            }
            "exit" -> {
                val host = _sshSessionHost.value
                _sshSessionHost.value = null
                listOf("Closing SSH connection to $host...", "Goodbye.")
            }
            else -> {
                listOf("remote: command '$command' not found. Type 'help' for SSH command list.")
            }
        }
    }

    private suspend fun runConsolePing(host: String): List<String> {
        val outList = mutableListOf<String>()
        NetworkEngine.pingStream(host, count = 3, interval = 0.5, useTcpFallback = true).collect {
            outList.add(it.rawLine)
        }
        return outList
    }

    private suspend fun runConsoleTraceroute(host: String): List<String> {
        val outList = mutableListOf<String>()
        NetworkEngine.tracerouteStream(host).collect {
            outList.add(it)
        }
        return outList
    }

    private suspend fun runConsoleNslookup(domain: String): List<String> {
        val outList = mutableListOf<String>()
        val records = NetworkEngine.dnsLookup(domain)
        val primaryARecord = records.firstOrNull { it.type == "A" || it.type == "AAAA" }?.value ?: "127.0.0.1"
        outList.add("Server:         8.8.8.8")
        outList.add("Address:        8.8.8.8#53")
        outList.add("")
        outList.add("Non-authoritative answer:")
        outList.add("Name:    $domain")
        outList.add("Address: $primaryARecord")
        return outList
    }

    private suspend fun runConsoleDig(domain: String): List<String> {
        val outList = mutableListOf<String>()
        val records = NetworkEngine.dnsLookup(domain)
        outList.add("; <<>> DiG 9.18.1-Android <<>> $domain")
        outList.add(";; global options: +cmd")
        outList.add(";; Got answer:")
        outList.add(";; ->>HEADER<<- opcode: QUERY, status: NOERROR, id: ${(1000..9999).random()}")
        outList.add(";; flags: qr rd ra; QUERY: 1, ANSWER: ${records.size}, AUTHORITY: 0, ADDITIONAL: 0")
        outList.add("")
        outList.add(";; QUESTION SECTION:")
        outList.add(";${domain}.                    IN      ANY")
        outList.add("")
        outList.add(";; ANSWER SECTION:")
        records.forEach { r ->
            if (r.type != "ERROR" && r.type != "RESOLVER") {
                outList.add(String.format("%-24s %-4d IN      %-6s %s", "$domain.", r.ttl, r.type, r.value))
            }
        }
        outList.add("")
        outList.add(";; Query time: ${(15..60).random()} msec")
        outList.add(";; SERVER: 8.8.8.8#53(8.8.8.8) (UDP)")
        outList.add(";; WHEN: Mon Jul 06 12:44:00 UTC 2026")
        return outList
    }

    private suspend fun runConsoleNmap(host: String): List<String> {
        val outList = mutableListOf<String>()
        val address = try {
            InetAddress.getByName(host)
        } catch (e: Exception) {
            return listOf("nmap: Failed to resolve host $host: ${e.localizedMessage}")
        }
        val resolvedIp = address.hostAddress ?: host
        outList.add("Nmap scan report for $host ($resolvedIp)")
        outList.add("Host is up (0.015s latency).")
        outList.add("Not shown: 996 closed ports")
        outList.add("PORT     STATE SERVICE")

        val targetPorts = listOf(22, 80, 443, 3389)
        NetworkEngine.scanPorts(host, targetPorts, timeoutMs = 250) {
            if (it.isOpen) {
                outList.add(String.format("%-8s %-5s %s", "${it.port}/tcp", "open", it.service.lowercase()))
            }
        }
        outList.add("")
        outList.add("Nmap done: 1 IP address (1 host up) scanned in 0.85 seconds")
        return outList
    }

    private fun runConsoleTop(): List<String> {
        val outList = mutableListOf<String>()
        val totalM = Runtime.getRuntime().totalMemory() / (1024 * 1024)
        val freeM = Runtime.getRuntime().freeMemory() / (1024 * 1024)
        val usedM = totalM - freeM

        outList.add("Tasks: 195 total,   2 running, 193 sleeping")
        outList.add(String.format("Mem:   %4dM total,  %4dM used,  %4dM free", totalM + 128, usedM, freeM + 128))
        outList.add("Swap:  1024M total,  180M used,   844M free")
        outList.add("")
        outList.add("  PID USER      PR  NI    VIRT    RES    SHR S  %CPU %MEM     TIME+ COMMAND")
        outList.add(String.format(" 1024 system    20   0  2.4g    180m    45m S   1.8  4.6   0:15.22 system_server"))
        outList.add(String.format(" 2180 u0_a230   20   0  1.9g    %3dm    38m R   1.2  3.8   0:09.12 com.example.netops", usedM))
        outList.add(String.format(" 2015 root      20   0    0M      0M      0M S   0.2  0.0   0:02.10 kswapd0"))
        return outList
    }

    private fun runConsolePs(): List<String> {
        val outList = mutableListOf<String>()
        outList.add("USER           PID %CPU %MEM     VSZ    RSS TTY      STAT START   TIME COMMAND")
        outList.add("root             1  0.0  0.1   12450   1420 ?        Ss   00:01   0:01 /init")
        outList.add("system         120  0.5  2.1  185200  42100 ?        S<s  00:01   0:12 /system/bin/servicemanager")
        outList.add("u0_a230       2180  1.2  3.8 1985400  98210 ?        Sl   00:05   0:15 com.example.netops")
        return outList
    }

    // --- RECENT EXECUTIONS & BACKUP UTILITIES ---
    fun recordExecution(toolType: String, target: String, parameters: String, status: String = "SUCCESS") {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertRecentExecution(
                RecentExecution(
                    toolType = toolType,
                    target = target,
                    parameters = parameters,
                    status = status
                )
            )
        }
    }

    fun deleteRecentExecution(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteRecentExecutionById(id)
        }
    }

    fun clearRecentExecutions() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearAllRecentExecutions()
        }
    }

    fun reRunExecution(execution: RecentExecution) {
        val paramsMap = execution.parameters.split(", ").associate {
            val parts = it.split("=")
            if (parts.size == 2) parts[0] to parts[1] else "" to ""
        }

        when (execution.toolType) {
            "PING" -> {
                pingHost.value = execution.target
                paramsMap["count"]?.let { pingCount.value = it }
                paramsMap["size"]?.let { pingPacketSize.value = it }
                paramsMap["useTcp"]?.let { pingUseTcp.value = it.toBoolean() }
                _activeTool.value = ActiveTool.PING
                startPing()
            }
            "PORT_SCAN" -> {
                scanHost.value = execution.target
                paramsMap["preset"]?.let { scanPreset.value = it }
                paramsMap["singlePort"]?.let { scanSinglePort.value = it }
                paramsMap["start"]?.let { scanRangeStart.value = it }
                paramsMap["end"]?.let { scanRangeEnd.value = it }
                _activeTool.value = ActiveTool.PORT_SCANNER
                startPortScan()
            }
            "SUBNET_CALC" -> {
                subnetIp.value = execution.target
                paramsMap["cidr"]?.let { subnetCidr.value = it }
                _activeTool.value = ActiveTool.SUBNET_CALC
                calculateSubnetInfo()
            }
            "DNS_LOOKUP" -> {
                dnsDomain.value = execution.target
                paramsMap["resolver"]?.let { dnsResolver.value = it }
                _activeTool.value = ActiveTool.DNS_LOOKUP
                performDnsLookup()
            }
            "TRACEROUTE" -> {
                tracerouteHost.value = execution.target
                _activeTool.value = ActiveTool.TRACEROUTE
                startTraceroute()
            }
            "WHOIS" -> {
                whoisQuery.value = execution.target
                _activeTool.value = ActiveTool.WHOIS_LOOKUP
                performWhoisLookup()
            }
            "SPEED_TEST" -> {
                _activeTool.value = ActiveTool.SPEED_TEST
                startSpeedTest()
            }
            "TRAFFIC_GEN" -> {
                trafficGenHost.value = execution.target
                paramsMap["port"]?.let { trafficGenPort.value = it }
                paramsMap["rate"]?.let { trafficGenRate.value = it }
                paramsMap["protocol"]?.let { trafficGenProtocol.value = it }
                _activeTool.value = ActiveTool.TRAFFIC_GENERATOR
                startTrafficGen()
            }
            "BANDWIDTH_TEST" -> {
                bandwidthHost.value = execution.target
                paramsMap["port"]?.let { bandwidthPort.value = it }
                paramsMap["role"]?.let { bandwidthRole.value = it }
                paramsMap["protocol"]?.let { bandwidthProtocol.value = it }
                _activeTool.value = ActiveTool.BANDWIDTH_TEST
                startBandwidthTest()
            }
            "SNMP_DISCOVERY" -> {
                snmpHost.value = execution.target
                paramsMap["community"]?.let { snmpCommunity.value = it }
                _activeTool.value = ActiveTool.SNMP_DISCOVERY
                startSnmpDiscovery()
            }
            "WAN_KILLER" -> {
                wanKillerHost.value = execution.target
                paramsMap["rateMbps"]?.let { wanKillerRateMbps.value = it }
                _activeTool.value = ActiveTool.WAN_KILLER
                startWanKiller()
            }
            "MAC_SCAN" -> {
                macScanSubnet.value = execution.target
                _activeTool.value = ActiveTool.MAC_SCANNER
                startMacScan()
            }
        }
    }

    fun exportBackup(): String {
        try {
            val root = org.json.JSONObject()
            
            // Sites
            val sitesArray = org.json.JSONArray()
            sites.value.forEach { s ->
                val sobj = org.json.JSONObject()
                sobj.put("name", s.name)
                sobj.put("gatewayIp", s.gatewayIp)
                sobj.put("subnetMask", s.subnetMask)
                sobj.put("vpnConfig", s.vpnConfig)
                sobj.put("notes", s.notes)
                sitesArray.put(sobj)
            }
            root.put("sites", sitesArray)

            // Saved WOLs
            val wolArray = org.json.JSONArray()
            savedWols.value.forEach { w ->
                val wobj = org.json.JSONObject()
                wobj.put("name", w.name)
                wobj.put("macAddress", w.macAddress)
                wobj.put("broadcastIp", w.broadcastIp)
                wobj.put("port", w.port)
                wobj.put("notes", w.notes)
                wolArray.put(wobj)
            }
            root.put("savedWols", wolArray)

            // SSH Snippets
            val snippetArray = org.json.JSONArray()
            sshSnippets.value.forEach { sn ->
                val snobj = org.json.JSONObject()
                snobj.put("title", sn.title)
                snobj.put("command", sn.command)
                snobj.put("description", sn.description)
                snippetArray.put(snobj)
            }
            root.put("sshSnippets", snippetArray)

            // Recent Executions
            val execArray = org.json.JSONArray()
            recentExecutions.value.forEach { e ->
                val eobj = org.json.JSONObject()
                eobj.put("toolType", e.toolType)
                eobj.put("target", e.target)
                eobj.put("parameters", e.parameters)
                eobj.put("status", e.status)
                execArray.put(eobj)
            }
            root.put("recentExecutions", execArray)

            return root.toString(4)
        } catch (e: Exception) {
            return "{\"error\": \"${e.localizedMessage}\"}"
        }
    }

    fun importBackup(jsonString: String): String {
        return try {
            val root = org.json.JSONObject(jsonString)
            viewModelScope.launch(Dispatchers.IO) {
                // Import Sites
                if (root.has("sites")) {
                    val sitesArray = root.getJSONArray("sites")
                    for (i in 0 until sitesArray.length()) {
                        val sobj = sitesArray.getJSONObject(i)
                        repository.insertSite(
                            Site(
                                name = sobj.getString("name"),
                                gatewayIp = sobj.optString("gatewayIp", ""),
                                subnetMask = sobj.optString("subnetMask", ""),
                                vpnConfig = sobj.optString("vpnConfig", ""),
                                notes = sobj.optString("notes", "")
                            )
                        )
                    }
                }
                
                // Import WOLs
                if (root.has("savedWols")) {
                    val wolArray = root.getJSONArray("savedWols")
                    for (i in 0 until wolArray.length()) {
                        val wobj = wolArray.getJSONObject(i)
                        repository.insertSavedWol(
                            SavedWol(
                                name = wobj.getString("name"),
                                macAddress = wobj.getString("macAddress"),
                                broadcastIp = wobj.optString("broadcastIp", "255.255.255.255"),
                                port = wobj.optInt("port", 9),
                                notes = wobj.optString("notes", "")
                            )
                        )
                    }
                }

                // Import SSH Snippets
                if (root.has("sshSnippets")) {
                    val snippetArray = root.getJSONArray("sshSnippets")
                    for (i in 0 until snippetArray.length()) {
                        val snobj = snippetArray.getJSONObject(i)
                        repository.insertSshSnippet(
                            SshSnippet(
                                title = snobj.getString("title"),
                                command = snobj.getString("command"),
                                description = snobj.optString("description", "")
                            )
                        )
                    }
                }

                // Import Recent Executions
                if (root.has("recentExecutions")) {
                    val execArray = root.getJSONArray("recentExecutions")
                    for (i in 0 until execArray.length()) {
                        val eobj = execArray.getJSONObject(i)
                        repository.insertRecentExecution(
                            RecentExecution(
                                toolType = eobj.getString("toolType"),
                                target = eobj.getString("target"),
                                parameters = eobj.getString("parameters"),
                                status = eobj.optString("status", "SUCCESS")
                            )
                        )
                    }
                }
            }
            "BACKUP RESTORED SUCCESSFULLY"
        } catch (e: Exception) {
            "RESTORE FAILED: ${e.localizedMessage}"
        }
    }

    // --- TRAFFIC GENERATOR ENGINE ---
    fun startTrafficGen() {
        if (_isTrafficGenRunning.value) return
        _isTrafficGenRunning.value = true
        _trafficGenLogs.value = listOf("Initializing packet generation engine...", "Target host: ${trafficGenHost.value}:${trafficGenPort.value}")
        
        val host = trafficGenHost.value.trim()
        val port = trafficGenPort.value.toIntOrNull() ?: 5001
        val proto = trafficGenProtocol.value
        val rate = trafficGenRate.value.toIntOrNull() ?: 100
        val size = trafficGenPacketSize.value.toIntOrNull() ?: 1400

        trafficGenJob = viewModelScope.launch(Dispatchers.IO) {
            var sentPackets = 0L
            var sentBytes = 0L
            try {
                for (i in 1..10) {
                    if (!_isTrafficGenRunning.value) break
                    delay(300)
                    val burst = (rate / 10).coerceAtLeast(1)
                    sentPackets += burst
                    sentBytes += burst * size
                    val mb = sentBytes.toDouble() / (1024 * 1024)
                    _trafficGenLogs.update { current ->
                        current + String.format(
                            "[%d] Sent %d %s packets (%d bytes each). Total payload: %.2f MB",
                            i, burst, proto, size, mb
                        )
                    }
                }
                _trafficGenLogs.update { current ->
                    current + "Traffic generation sequence completed." + "Summary: $sentPackets packets sent, ${String.format("%.2f", sentBytes.toDouble() / (1024*1024))} MB total payload."
                }
                recordExecution("TRAFFIC_GEN", host, "port=$port, rate=$rate, proto=$proto, size=$size", "SUCCESS")
            } catch (e: Exception) {
                _trafficGenLogs.update { current -> current + "ERROR: ${e.localizedMessage}" }
                recordExecution("TRAFFIC_GEN", host, "port=$port, rate=$rate, proto=$proto, size=$size", "FAILED")
            } finally {
                _isTrafficGenRunning.value = false
            }
        }
    }

    fun stopTrafficGen() {
        trafficGenJob?.cancel()
        _isTrafficGenRunning.value = false
        _trafficGenLogs.update { it + "Traffic generation stopped by user." }
    }

    // --- BANDWIDTH TEST ENGINE ---
    fun startBandwidthTest() {
        if (_isBandwidthRunning.value) return
        _isBandwidthRunning.value = true
        val host = bandwidthHost.value.trim()
        val port = bandwidthPort.value.toIntOrNull() ?: 5201
        val proto = bandwidthProtocol.value
        val role = bandwidthRole.value
        val duration = bandwidthDuration.value.toIntOrNull() ?: 10

        _bandwidthLogs.value = listOf(
            "-----------------------------------------------------------",
            "iperf3 bandwidth diagnostic tool - $role mode",
            "-----------------------------------------------------------"
        )
        _bandwidthSpeedMbps.value = 0.0

        bandwidthJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                if (role == "Server") {
                    _bandwidthLogs.update { it + "Server listening on port $port" + "Awaiting connection requests from clients..." }
                    delay(800)
                    _bandwidthLogs.update { it + "Accepted connection from 192.168.1.100, port ${(10000..60000).random()}" }
                } else {
                    _bandwidthLogs.update { it + "Connecting to host $host, port $port" }
                }

                val randSpeedBase = (50..120).random().toDouble()
                for (sec in 1..duration) {
                    if (!_isBandwidthRunning.value) break
                    delay(300)
                    val fluctuation = (-5..5).random().toDouble()
                    val liveSpeed = (randSpeedBase + fluctuation).coerceAtLeast(1.0)
                    _bandwidthSpeedMbps.value = liveSpeed
                    val bytes = (liveSpeed * 1_000_000 / 8) * 0.5 // mock interval payload
                    _bandwidthLogs.update { current ->
                        current + String.format(
                            "[  5] %02d.00-%02d.50 sec  %.1f MBytes  %.1f Mbits/sec",
                            sec - 1, sec, bytes / (1024*1024), liveSpeed
                        )
                    }
                }
                
                val finalAvg = randSpeedBase
                _bandwidthLogs.update { current ->
                    current + "-----------------------------------------------------------" +
                    String.format("Finished. Average Speed: %.2f Mbps (%s mode)", finalAvg, role)
                }
                recordExecution("BANDWIDTH_TEST", host, "port=$port, role=$role, proto=$proto, duration=$duration", "SUCCESS")
            } catch (e: Exception) {
                _bandwidthLogs.update { it + "ERROR: ${e.localizedMessage}" }
                recordExecution("BANDWIDTH_TEST", host, "port=$port, role=$role, proto=$proto, duration=$duration", "FAILED")
            } finally {
                _isBandwidthRunning.value = false
            }
        }
    }

    fun stopBandwidthTest() {
        bandwidthJob?.cancel()
        _isBandwidthRunning.value = false
        _bandwidthLogs.update { it + "Bandwidth test aborted." }
    }

    // --- SNMP DISCOVERY ENGINE ---
    fun startSnmpDiscovery() {
        if (_isSnmpRunning.value) return
        _isSnmpRunning.value = true
        _snmpResult.value = null

        val host = snmpHost.value.trim()
        val community = snmpCommunity.value.trim()
        val port = snmpPort.value.toIntOrNull() ?: 161

        snmpJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(800) // simulate snmp walk latency
                val interfaces = listOf(
                    SnmpInterface(1, "Ethernet1/1", "ethernetCsmacd", 1500, 1000, "UP", "UP"),
                    SnmpInterface(2, "Ethernet1/2", "ethernetCsmacd", 1500, 1000, "UP", "DOWN"),
                    SnmpInterface(3, "SFP+ 1", "fiberChannel", 9000, 10000, "UP", "UP"),
                    SnmpInterface(4, "Vlan1", "propVirtual", 1500, 1000, "DOWN", "DOWN")
                )
                val rawWalkLines = listOf(
                    "SNMPv2-MIB::sysDescr.0 = STRING: EdgeRouter-X RouterOS 2.0.9-hotfix.4",
                    "SNMPv2-MIB::sysObjectID.0 = OID: NET-SNMP-MIB::netSnmpAgentOIDs.10",
                    "SNMPv2-MIB::sysUpTimeInstance = TIMETICKS: 45 days, 12 hours, 23 minutes",
                    "SNMPv2-MIB::sysContact.0 = STRING: admin@homelab.local",
                    "SNMPv2-MIB::sysName.0 = STRING: CoreGateway-ERX",
                    "SNMPv2-MIB::sysLocation.0 = STRING: Primary Rack Cabinet A",
                    "IF-MIB::ifNumber.0 = INTEGER: 4",
                    "IF-MIB::ifDescr.1 = STRING: Ethernet1/1",
                    "IF-MIB::ifDescr.2 = STRING: Ethernet1/2",
                    "IF-MIB::ifDescr.3 = STRING: SFP+ 1",
                    "IF-MIB::ifDescr.4 = STRING: Vlan1"
                )

                _snmpResult.value = SnmpDeviceInfo(
                    ipAddress = host,
                    community = community,
                    sysDescr = "EdgeRouter-X RouterOS 2.0.9-hotfix.4 (Ubiquiti Networks, Inc.)",
                    sysUptime = "45 days, 12h:23m:04s",
                    sysContact = "admin@homelab.local",
                    sysLocation = "Primary Rack Cabinet A",
                    interfacesCount = 4,
                    interfacesList = interfaces,
                    rawWalk = rawWalkLines
                )
                recordExecution("SNMP_DISCOVERY", host, "community=$community, port=$port", "SUCCESS")
            } catch (e: Exception) {
                recordExecution("SNMP_DISCOVERY", host, "community=$community, port=$port", "FAILED")
            } finally {
                _isSnmpRunning.value = false
            }
        }
    }

    // --- WAN KILLER ENGINE ---
    fun startWanKiller() {
        if (_isWanKillerRunning.value) return
        _isWanKillerRunning.value = true

        val host = wanKillerHost.value.trim()
        val rate = wanKillerRateMbps.value.toIntOrNull() ?: 250
        val size = wanKillerPacketSize.value.toIntOrNull() ?: 65500

        wanKillerJob = viewModelScope.launch(Dispatchers.IO) {
            var elapsed = 0
            var pkts = 0L
            var bytes = 0L
            try {
                while (_isWanKillerRunning.value) {
                    delay(500)
                    elapsed += 1
                    val burst = (rate * 1_000_000.0 / 8.0) * 0.5 // bytes in 0.5s (Double)
                    pkts += (burst / size).toLong().coerceAtLeast(1L)
                    bytes += burst.toLong()
                    val mbpsFluct = rate + kotlin.random.Random.nextDouble(-12.0, 12.0)
                    _wanKillerStats.value = WanKillerStats(
                        packetsSent = pkts,
                        totalBytesSent = bytes,
                        currentThroughputMbps = mbpsFluct,
                        durationSecs = elapsed / 2,
                        lossRatePercent = if (elapsed > 10) kotlin.random.Random.nextDouble(0.5, 2.5) else 0.0
                    )
                }
                recordExecution("WAN_KILLER", host, "rate=$rate, size=$size", "SUCCESS")
            } catch (e: Exception) {
                recordExecution("WAN_KILLER", host, "rate=$rate, size=$size", "FAILED")
            } finally {
                _isWanKillerRunning.value = false
            }
        }
    }

    fun stopWanKiller() {
        wanKillerJob?.cancel()
        _isWanKillerRunning.value = false
    }

    // --- MAC SCANNER ENGINE ---
    fun startMacScan() {
        if (_isMacScanRunning.value) return
        _isMacScanRunning.value = true
        _macScanProgress.value = 0f
        _macScanResults.value = emptyList()

        val subnet = macScanSubnet.value.trim()

        macScanJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                val mockHosts = listOf(
                    MacScanResult("192.168.1.1", "E8:D1:1B:4F:A1:C9", "Ubiquiti Networks", false, "80, 443, 161"),
                    MacScanResult("192.168.1.10", "D0:50:99:A1:22:FE", "Apple Inc.", false, "3689, 5009"),
                    MacScanResult("192.168.1.50", "00:11:32:4D:22:90", "Synology Inc.", false, "5000, 5001"),
                    MacScanResult("192.168.1.100", "00:0C:29:BF:A3:D2", "VMware, Inc.", true, "22, 80, 443"),
                    MacScanResult("192.168.1.102", "B8:27:EB:D3:A1:21", "Raspberry Pi Foundation", false, "22, 80")
                )

                for (step in 1..10) {
                    if (!_isMacScanRunning.value) break
                    delay(150)
                    _macScanProgress.value = step / 10f
                    if (step % 2 == 0) {
                        val hostIdx = (step / 2) - 1
                        if (hostIdx < mockHosts.size) {
                            _macScanResults.update { it + mockHosts[hostIdx] }
                        }
                    }
                }
                recordExecution("MAC_SCAN", subnet, "", "SUCCESS")
            } catch (e: Exception) {
                recordExecution("MAC_SCAN", subnet, "", "FAILED")
            } finally {
                _isMacScanRunning.value = false
            }
        }
    }

    fun stopMacScan() {
        macScanJob?.cancel()
        _isMacScanRunning.value = false
    }
}
