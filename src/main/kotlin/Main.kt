fun main(args: Array<String>) {
    println("Program arguments: ${args.joinToString()}")
    println("Welcome to my hue application!")
    while (menuIterator > 0) {
        printMenu()
    }
}

// global var to keep track of bridge data
val bridgeDataObject: BridgeData = BridgeData()
var menuIterator: Int = 1

fun printMenu() {
    println("\nChoose any of the following menu items----")
    println("fetch :: fetch the base ip of the bridge--")
    println("showIP:: show the current bridge ip-------")
    println("register :: register the app on the bridge")
    println("list-lights :: (wip) list all lights on the bridge")
    println("exit  :: exits the application -----------")
    println("------------------------------------------")
    print("input: ")
    when(readLine()) {
        "fetch" -> bridgeDataObject.fetchHueBridgeData()
        "showIP" -> println("Current bridge IP: ${bridgeDataObject.globalBridgeIpAddress}")
        "register" -> bridgeDataObject.createApiResourceOnBridge()
        "list-lights" -> bridgeDataObject.getLightsFromAPI()
        "exit" -> menuIterator = -1                            // by setting the iter to -1 we exit the menu loop
        else -> println("Invalid input! Try again!")
    }
}