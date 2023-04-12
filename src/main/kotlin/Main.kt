import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

fun main(args: Array<String>) {
    println("Program arguments: ${args.joinToString()}")
    println("Welcome to my hue application!")
    printMenu()
}

fun printMenu() {
    println("--choose any of the following menu items--")
    println("fetch :: fetch the base ip of the bridge--")
    println("------------------------------------------")
    print("input: ")
    val userInput = readLine();

    if(userInput.equals("fetch")) {
        fetchBaseData()
    }
}

fun fetchBaseData() {
    println("... Sending requests.. : ")
    sendRequest()
}

fun sendRequest() {
    val url = URL("https://discovery.meethue.com/")
    val connection = url.openConnection()
    BufferedReader(InputStreamReader(connection.getInputStream())).use { inp ->
        var line: String?
        while (inp.readLine().also { line = it } != null) {
            println(line)
        }
    }
}