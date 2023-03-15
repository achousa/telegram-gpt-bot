/**
 * This class represents a client that interacts with the OpenAI API to get a chat response.
 */
package es.achousa.clients;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.achousa.model.request.ChatRequest;
import es.achousa.model.response.ChatResponse;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Service
public class OpenAIApiClient {

    public final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    @Value("${openai.apikey}")
    private String apiKey;
    @Value("${openai.url}")
    private String url;
    private OkHttpClient client;

    /**
     * Initializes the OkHttpClient with a timeout of 50 seconds for each operation.
     */
    @PostConstruct
    private void init() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(50, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Sends a chat request to the OpenAI API and returns a chat response.
     * @param chatRequest the chat request to be sent.
     * @return a chat response from the OpenAI API.
     * @throws Exception if the response is not successful or if there is a problem with the request.
     */
    public ChatResponse getCompletion(ChatRequest chatRequest) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        RequestBody body = RequestBody.create(mapper.writeValueAsString(chatRequest), JSON);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new Exception("Unexpected code " + response);
        ChatResponse chatResponse = mapper.readValue(response.body().string(), ChatResponse.class);
        return chatResponse;
    }
}
