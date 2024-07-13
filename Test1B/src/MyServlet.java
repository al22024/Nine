import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@WebServlet("/api/data")
public class MyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public static List<JSONObject> jsonDataList = new ArrayList<>();

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }

        System.out.println("Request Body: " + requestBody.toString());

        try {
            JSONParser parser = new JSONParser();
            JSONObject requestData = (JSONObject) parser.parse(requestBody.toString());
            String action = (String) requestData.get("action");

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "POST received");
            jsonResponse.put("data", requestData);

            if ("event".equals(action)) {
                EventRegister eventRegister = new EventRegister();
                int eventResponse = eventRegister.sendData(requestData);
                jsonResponse.put("projectID", eventResponse);
            } else if ("join".equals(action)) {
                JoinRegister joinRegister = new JoinRegister();
                String status = joinRegister.sendData(Integer.parseInt((String) requestData.get("projectID")),(String) requestData.get("displayName"),((Number)requestData.get("userID")).intValue());
                jsonResponse.put("progressstatus", status);
            } else if("dispose".equals(action)) {
                EventRegister eventRegister = new EventRegister();
                eventRegister.disposeData(requestData);
            } else if("decline".equals(action)) {
                JoinRegister joinRegister = new JoinRegister();
                joinRegister.disposeData(requestData);
            }
            /*企画選択からの遷移
             else if("move".equals(action)){
             	int projectID = Integer.parseInt((String) requestData.get("projectID"));
             	int userID = Integer.parseInt((String) requestData.get("userID"))
             	HttpSession session = request.getSession();
  				session.setAttribute("projectID", projectID);
             	ProjectInfo cons = new ProjectInfo();
             	ProjectInfo project = cons.getProjectInfo();
             	String progressstatus = project.progressstatus;
             	UserAndProjectInfo userandprojectinfo =new UserAndProjectInfo();
            	UserAndProjectInfo user = userandprojectinfo.getVoteInfo(projectID,userID);
            	String genre = user.genre;
             	if("Registration".equals(progressstatus)){
             		(締め切り画面へ遷移)
             	}else if("Matching".equals(progressstatus)){
             		(日程画面へ遷移)
             	}else if("Searching".equals(progressstatus)){
             		(検索画面へ遷移)
             	}else if("Voting".equals(progressstatus)){
             		if(genre==null){
             			(投票画面へ遷移)
             		}else{
             			(投票修正画面へ遷移)
             		}
             	}else if("Notification".equals(progressstatus)){
             		(決定画面へ遷移)
             	}
             締め切り操作による遷移
            }else if("close".equals(action)){
            	int projectID = Integer.parseInt((String) requestData.get("projectID"));
            	HttpSession session = request.getSession();
            	session.setAttribute("projectID", projectID);
            	ProjectInfo cons = new ProjectInfo();
             	ProjectInfo project = cons.getProjectInfo(projectID);
             	project.progressstatus="Matching";
             	project.setProjectInfo();
             	(日程画面へ遷移)
            }
             */

            String jsonResponseString = jsonResponse.toJSONString();
            response.setContentType("application/json");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setStatus(HttpServletResponse.SC_OK);
            try (OutputStream os = response.getOutputStream()) {
                os.write(jsonResponseString.getBytes(StandardCharsets.UTF_8));
            }

        } catch (ParseException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        JSONArray jsonArray = new JSONArray();

        try {
            String projectIDParam = request.getParameter("projectID");
            String userIDParam = request.getParameter("userID");

            if (projectIDParam != null && userIDParam==null) {
                int requestedProjectID = Integer.parseInt(projectIDParam);

                // Search for matching projectID in jsonDataList
                for (JSONObject data : jsonDataList) {
                    int projectID = ((Integer) data.get("projectID")).intValue(); // Cast to int from Long
                    if (projectID == requestedProjectID) {
                        jsonArray.add(data);
                    }
                }
            } else {
                // If no projectID parameter provided, return all data
                for (JSONObject data : jsonDataList) {
                    jsonArray.add(data);
                }
            }
            if(projectIDParam != null && userIDParam != null) {
            	int projectID = Integer.parseInt(projectIDParam);
            	int userID = Integer.parseInt(userIDParam);
            	JSONObject data = new JSONObject();
            	ProjectInfo cons = new ProjectInfo();
            	ProjectInfo project = cons.getProjectInfo(projectID);
            	UserAndProjectInfo userandprojectinfo =new UserAndProjectInfo();
            	UserAndProjectInfo user = userandprojectinfo.getVoteInfo(projectID,userID);
            	String progressstatus = project.progressstatus;
            	String genre = user.genre;
            	data.put("progressstatus",progressstatus);
            	data.put("genre",genre);
            	jsonArray.add(genre);
            }

            // Prepare response
            String jsonDataString = jsonArray.toJSONString();
            System.out.println("response"+jsonDataString);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            try (OutputStream os = response.getOutputStream()) {
                os.write(jsonDataString.getBytes(StandardCharsets.UTF_8));
            }
        } catch (NumberFormatException e) {
            // Handle invalid projectID parameter
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid projectID parameter");
            e.printStackTrace(); // Optionally log the exception
        } catch (Exception e) {
            // Handle other exceptions
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Internal server error");
            e.printStackTrace(); // Optionally log the exception
        }
    }

}
