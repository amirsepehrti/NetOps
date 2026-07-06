package com.example.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "sites")
data class Site(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val gatewayIp: String = "",
    val subnetMask: String = "",
    val vpnConfig: String = "",
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "devices")
data class Device(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val siteId: Int,
    val name: String,
    val ipAddress: String,
    val deviceType: String, // Router, Switch, Server, VM, Other
    val vendor: String, // MikroTik, Cisco, Proxmox, Generic
    val notes: String = ""
)

@Entity(tableName = "saved_wols")
data class SavedWol(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val macAddress: String,
    val broadcastIp: String = "255.255.255.255",
    val port: Int = 9,
    val notes: String = ""
)

@Entity(tableName = "ssh_snippets")
data class SshSnippet(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val command: String,
    val description: String = ""
)

@Entity(tableName = "recent_executions")
data class RecentExecution(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val toolType: String, // PING, PORT_SCAN, SUBNET_CALC, DNS_LOOKUP, WAKE_ON_LAN, TRACEROUTE, WHOIS, SPEED_TEST, TRAFFIC_GEN, BANDWIDTH_TEST, SNMP_DISCOVERY, WAN_KILLER, MAC_SCAN
    val target: String,
    val parameters: String, // "count=4" or "ports=80,443" etc
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "SUCCESS"
)

@Dao
interface NetOpsDao {
    // Sites
    @Query("SELECT * FROM sites ORDER BY timestamp DESC")
    fun getAllSites(): Flow<List<Site>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSite(site: Site): Long

    @Query("DELETE FROM sites WHERE id = :id")
    suspend fun deleteSiteById(id: Int)

    // Devices
    @Query("SELECT * FROM devices WHERE siteId = :siteId")
    fun getDevicesForSite(siteId: Int): Flow<List<Device>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevice(device: Device)

    @Query("DELETE FROM devices WHERE id = :id")
    suspend fun deleteDeviceById(id: Int)

    // Saved WoLs
    @Query("SELECT * FROM saved_wols ORDER BY name ASC")
    fun getAllSavedWols(): Flow<List<SavedWol>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedWol(savedWol: SavedWol)

    @Query("DELETE FROM saved_wols WHERE id = :id")
    suspend fun deleteSavedWolById(id: Int)

    // SSH Snippets
    @Query("SELECT * FROM ssh_snippets ORDER BY title ASC")
    fun getAllSshSnippets(): Flow<List<SshSnippet>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSshSnippet(snippet: SshSnippet)

    @Query("DELETE FROM ssh_snippets WHERE id = :id")
    suspend fun deleteSshSnippetById(id: Int)

    // Recent Executions
    @Query("SELECT * FROM recent_executions ORDER BY timestamp DESC LIMIT 30")
    fun getAllRecentExecutions(): Flow<List<RecentExecution>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentExecution(execution: RecentExecution): Long

    @Query("DELETE FROM recent_executions WHERE id = :id")
    suspend fun deleteRecentExecutionById(id: Int)

    @Query("DELETE FROM recent_executions")
    suspend fun clearAllRecentExecutions()
}

@Database(entities = [Site::class, Device::class, SavedWol::class, SshSnippet::class, RecentExecution::class], version = 2, exportSchema = false)
abstract class NetOpsDatabase : RoomDatabase() {
    abstract fun netOpsDao(): NetOpsDao
}
