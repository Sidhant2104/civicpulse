@Service
public class IssueService {

    public IssueResponseDto createIssue(CreateIssueRequestDto dto, String role) {

        //VALIDATION
        if(!role.equals("CITIZEN")){
            throw new RuntimeException("Only Citizen can create issue.");
        }

        String description = dto.getDescription();
        String desc = description.toLowerCase();
        String department;

        if(desc.contains("water")){
            
        }

        return null;
    }
}