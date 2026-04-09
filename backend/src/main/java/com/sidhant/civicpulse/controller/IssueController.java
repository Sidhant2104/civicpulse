@RestController
public class IssueController {

    private IssueService issueService;

    public IssueController(IssueService issueService){
        this.issueService = issueService;
    }

    public IssueResponseDto createIssue(@RequestBody CreateIssueRequestDto dto, 
        @RequestHeader("X-USER-ROLE") String role){
        return issueService.createIssue(dto, role);
    }
    
}
