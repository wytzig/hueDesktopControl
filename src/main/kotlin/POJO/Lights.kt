package POJO

import java.util.*

class Lights {
    var lights: List<Light> = Collections.emptyList()
}

data class Light(
    val state: State,
    val swupdate: SwupdateState,
    val type: String,
    val name: String,
    val modelid: String,
    val manufacturername: String,
    val productname: String,
    val capabilities: Capabilities,
    val config: Config,
    val uniqueid: String,
    val swversion: String,
    val swconfigid: String,
    val productid: String
)

data class State(
    val on: Boolean,
    val bri: Int,
    val hue: Int,
    val sat: Int,
    val effect: String,
    val xy: List<Double>,
    val ct: Int,
    val alert: String,
    val colormode: String,
    val mode: String,
    val reachable: Boolean
)

data class SwupdateState(
    val state: String,
    val lastinstall: String
)

data class ControlCT(
    val min: Int,
    val max: Int
)

data class Control(
    val mindimlevel: Int,
    val maxlumen: Int,
    val colorgamuttype: String,
    val colorgamut: List<List<Double>>,
    val ct: ControlCT
)

data class Streaming(
    val renderer: Boolean,
    val proxy: Boolean
)

data class Capabilities(
    val certified: Boolean,
    val control: Control,
    val streaming: Streaming
)

data class Startup(
    val mode: String,
    val configured: Boolean
)

data class Config(
    val archetype: String,
    val function: String,
    val direction: String,
    val startup: Startup
)