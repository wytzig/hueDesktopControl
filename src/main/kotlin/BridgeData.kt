import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.URL
import java.net.URLConnection


class BridgeData() {

    companion object {
        var globalBridgeIpAddress: String = ""
    }

    fun fetchHueBridgeData() {
        val url = URL("https://discovery.meethue.com/") // init
        val connection = url.openConnection()

        // try using discovery.meethue.com, but this can give a 429 if you try this too often
        val requestResult = tryToSendRequest(connection) //todo implement more discover / auth fallback options

        if(requestResult.isNullOrEmpty()) {
            return
        }
        val jsonResult = JSONArray(requestResult)

        if(!jsonResult.isEmpty) {
            globalBridgeIpAddress = jsonResult.get(1).toString()
            println("global Bridge IP = $globalBridgeIpAddress")
        }
    }

    /**
     * Whatever happens, we return a result!
     */
    fun tryToSendRequest(connection: URLConnection): String {
        var result: String = ""
        try{
            BufferedReader(InputStreamReader(connection.getInputStream())).use { inp ->
                var line: String?
                while (inp.readLine().also { line = it } != null) {
                    if (line != null) {
                        result += line
                    }
                }
                println(result) //todo remove
            }
        } catch (e : Exception) {
            println("$e")
        } finally {
            return result
        }
    }
}