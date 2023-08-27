import POJO.Lights
import com.google.gson.Gson
import org.json.JSONArray
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.URL
import java.net.URLConnection


class BridgeData {

    var globalBridgeIpAddress: String = ""
    var bridgeApiDeviceTypeUsername: String = "smqH3CWNhF55Sp3e9zvSRFz0wPnEJtqTwL0IKEhX"
    val bridgeApiDeviceType: String = "my_hue_app#my_desktop_pc"

    fun getLightsFromAPI() {
        val url = URL("http://$globalBridgeIpAddress/api/$bridgeApiDeviceTypeUsername/lights")
        val connection = url.openConnection()
        val result = tryToSendRequest(connection)

        println("request result: $result") //todo remove
        val gson = Gson()

        // Parse the JSON string into the Lights class
        val lights: Lights = gson.fromJson(result, Lights::class.java)
        for(light in lights.lights) {
            println("Light: ${light.name} is ${light.state.on}")
        }
    }

    fun createApiResourceOnBridge() {
        if(bridgeApiDeviceTypeUsername != "") {
            println("You already have a username! $bridgeApiDeviceTypeUsername")
            return
        }
        val url = URL("http://$globalBridgeIpAddress/api/")
        val postData = "{\"devicetype\":\"$bridgeApiDeviceType\"}"
        val connection = url.openConnection()

        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "application/json")
        connection.setRequestProperty("Content-Length", postData.length.toString())
        DataOutputStream(connection.getOutputStream()).use { it.writeBytes(postData) }

        val result = tryToSendRequest(connection)

        if (checkIfResponseIsSecurityKey(result)) {
            println("Please press the link button on the bridge and register again!")
            return
        }

        println("request result: $result") //todo remove
        bridgeApiDeviceTypeUsername = getUserNameFromResponse(result)
        println("Distilled username is $bridgeApiDeviceTypeUsername")
    }

    private fun checkIfResponseIsSecurityKey(response: String): Boolean {
        return response.contains("link button not pressed")
    }

    private fun getUserNameFromResponse(response: String): String {
        return response.split(",")[1].split(":")[1].replace("\"", "")
    }

    fun fetchHueBridgeData() {

        // only if we don't know the ip yet we should try to fetch it
        if (this.globalBridgeIpAddress == "") {
            println("... No known IP.. Fetching bridge data...")
            val url = URL("https://discovery.meethue.com/") // init
            val connection = url.openConnection()

            // try using discovery.meethue.com, but this can give a 429 if you try this too often
            val requestResult = tryToSendRequest(connection) //todo implement more discover / auth fallback options

            if (requestResult.isNullOrEmpty()) {
                return
            }
            val jsonResultArray = JSONArray(requestResult)

            if (!jsonResultArray.isEmpty) {
                val jsonResult = jsonResultArray.get(0)
                globalBridgeIpAddress = jsonResult.toString().split(",")[0].split(":")[1].replace(
                    "\"",
                    ""
                ) // I'm lucky that the ip was not in the first position
            }
        }
        println("global Bridge found! ~IP = $globalBridgeIpAddress")
    }

    /**
     * Whatever happens, we return a result!
     */
    private fun tryToSendRequest(connection: URLConnection): String {
        var result: String = ""
        try {
            BufferedReader(InputStreamReader(connection.getInputStream())).use { inp ->
                var line: String?
                while (inp.readLine().also { line = it } != null) {
                    if (line != null) {
                        result += line
                    }
                }
            }
        } catch (e: Exception) {
            println("$e")
        } finally {
            return result
        }
    }
}