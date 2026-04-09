
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public String Test(){
        return "Civicpulse backend running";
    }
    
}
