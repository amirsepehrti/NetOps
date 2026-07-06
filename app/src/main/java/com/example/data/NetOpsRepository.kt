package com.example.data

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class NetOpsRepository(context: Context) {
    private val db: NetOpsDatabase = Room.databaseBuilder(
        context.applicationContext,
        NetOpsDatabase::class.java,
        "netops_db"
    ).fallbackToDestructiveMigration().build()

    private val dao = db.netOpsDao()

    init {
        // Prepopulate default data if empty
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val sites = dao.getAllSites().first()
                if (sites.isEmpty()) {
                    val homelabId = dao.insertSite(
                        Site(
                            name = "Default Homelab",
                            gatewayIp = "192.168.1.1",
                            subnetMask = "24",
                            vpnConfig = "WireGuard Tunnel",
                            notes = "Primary home testing environment"
                        )
                    ).toInt()

                    val officeId = dao.insertSite(
                        Site(
                            name = "HQ Office",
                            gatewayIp = "10.0.0.1",
                            subnetMask = "22",
                            vpnConfig = "IPsec Site-to-Site",
                            notes = "Corporate headquarter network"
                        )
                    ).toInt()

                    // Insert Default Devices for Homelab
                    dao.insertDevice(
                        Device(
                            siteId = homelabId,
                            name = "Core Router (hEX)",
                            ipAddress = "192.168.1.1",
                            deviceType = "Router",
                            vendor = "MikroTik",
                            notes = "Main edge gateway"
                        )
                    )
                    dao.insertDevice(
                        Device(
                            siteId = homelabId,
                            name = "Access Switch (24G)",
                            ipAddress = "192.168.1.2",
                            deviceType = "Switch",
                            vendor = "Cisco",
                            notes = "Distribution layer"
                        )
                    )
                    dao.insertDevice(
                        Device(
                            siteId = homelabId,
                            name = "Hypervisor Node 1",
                            ipAddress = "192.168.1.100",
                            deviceType = "Server",
                            vendor = "Proxmox",
                            notes = "Runs DNS, Docker, and NAS"
                        )
                    )

                    // Insert Default Devices for Office
                    dao.insertDevice(
                        Device(
                            siteId = officeId,
                            name = "Edge Firewall",
                            ipAddress = "10.0.0.1",
                            deviceType = "Router",
                            vendor = "Generic",
                            notes = "pfSense Security Gateway"
                        )
                    )

                    // Insert Default WOL targets
                    dao.insertSavedWol(
                        SavedWol(
                            name = "Media Server (Plex)",
                            macAddress = "3C:7C:3F:8A:2B:9C",
                            broadcastIp = "192.168.1.255",
                            port = 9,
                            notes = "Living room server"
                        )
                    )
                    dao.insertSavedWol(
                        SavedWol(
                            name = "Backup NAS",
                            macAddress = "00:11:32:A1:B2:C3",
                            broadcastIp = "192.168.1.255",
                            port = 9,
                            notes = "Synology RackStation"
                        )
                    )

                    // Insert default SSH Snippets
                    dao.insertSshSnippet(
                        SshSnippet(
                            title = "System Resource Check",
                            command = "uname -a && df -h && free -h",
                            description = "View OS version, disk spaces, and RAM usage stats"
                        )
                    )
                    dao.insertSshSnippet(
                        SshSnippet(
                            title = "List Active Containers",
                            command = "docker ps --format \"table {{.Names}}\\t{{.Status}}\\t{{.Ports}}\"",
                            description = "View names, uptime, and port mappings of running containers"
                        )
                    )
                    dao.insertSshSnippet(
                        SshSnippet(
                            title = "MikroTik CLI Resources",
                            command = "/system resource print",
                            description = "View RouterOS CPU, RAM, and disk specs"
                        )
                    )
                    dao.insertSshSnippet(
                        SshSnippet(
                            title = "View Route Table",
                            command = "ip route show || route -n",
                            description = "Print the routing gateway rules of the remote OS"
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Site methods
    val allSites: Flow<List<Site>> = dao.getAllSites()
    suspend fun insertSite(site: Site) = dao.insertSite(site)
    suspend fun deleteSiteById(id: Int) = dao.deleteSiteById(id)

    // Device methods
    fun getDevicesForSite(siteId: Int): Flow<List<Device>> = dao.getDevicesForSite(siteId)
    suspend fun insertDevice(device: Device) = dao.insertDevice(device)
    suspend fun deleteDeviceById(id: Int) = dao.deleteDeviceById(id)

    // Saved WOL methods
    val allSavedWols: Flow<List<SavedWol>> = dao.getAllSavedWols()
    suspend fun insertSavedWol(savedWol: SavedWol) = dao.insertSavedWol(savedWol)
    suspend fun deleteSavedWolById(id: Int) = dao.deleteSavedWolById(id)

    // SSH Snippet methods
    val allSshSnippets: Flow<List<SshSnippet>> = dao.getAllSshSnippets()
    suspend fun insertSshSnippet(snippet: SshSnippet) = dao.insertSshSnippet(snippet)
    suspend fun deleteSshSnippetById(id: Int) = dao.deleteSshSnippetById(id)

    // Recent Execution methods
    val allRecentExecutions: Flow<List<RecentExecution>> = dao.getAllRecentExecutions()
    suspend fun insertRecentExecution(execution: RecentExecution) = dao.insertRecentExecution(execution)
    suspend fun deleteRecentExecutionById(id: Int) = dao.deleteRecentExecutionById(id)
    suspend fun clearAllRecentExecutions() = dao.clearAllRecentExecutions()
}
