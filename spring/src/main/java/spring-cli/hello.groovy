@RestController
class WebApplication {

    @GetMapping("/")
    String home() {
        "Hello world!"
    }
}