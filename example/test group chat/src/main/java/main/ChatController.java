package main;

import main.dto.DtoMessage;
import main.dto.MessageMapper;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
public class ChatController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageMapper messageMapper;

    @GetMapping("/init")
    public HashMap<String, Boolean> init(){
        HashMap<String, Boolean> response = new HashMap<>();
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        Optional<User> userOptional = userRepository.findBySessionId(sessionId);

        response.put("result", userOptional.isPresent());
        return  response;
    }

    @PostMapping("/auth")
    public HashMap<String, Boolean> auth(@RequestParam String name){
        HashMap<String, Boolean> response = new HashMap<>();

        if (!Strings.isNotEmpty(name)) {
            response.put("result", false);
            return response;
        }

        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        User user = new User();

        user.setName(name);
        user.setSessionId(sessionId);
        userRepository.save(user);

        response.put("result", true);
        return  response;
    }

    @PostMapping("/message")
    public Map<String, Boolean> sendMessage(@RequestParam String message){


        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        User user = userRepository.findBySessionId(sessionId).get();

        if (Strings.isEmpty(message)){
            return Map.of("result", false);
        }

        Message message1 = new Message();
        message1.setMessage(message);
        message1.setDateTime(LocalDateTime.now());
        message1.setUser(user);
        messageRepository.save(message1);

        return Map.of("result", true);
    }

    @GetMapping("/message")
    public List<DtoMessage> getMessagesList(){

        List<DtoMessage> result = messageRepository.
                findAll(Sort.by(Sort.Direction.ASC, "datetime")).stream()
                .map(message -> MessageMapper.map(message)).collect(Collectors.toList());

        return new ArrayList<>();
    }

    @GetMapping("/user")
    public HashMap<Integer, String> getUsersList(){
        return null;
    }
}
