package lchat.pccontroller.data

sealed class ConnectionTestResult {
    object Success : ConnectionTestResult()
    object Timeout : ConnectionTestResult()
    data class Error(val message: String) : ConnectionTestResult()
}