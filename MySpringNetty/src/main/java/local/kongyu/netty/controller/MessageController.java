package local.kongyu.netty.controller;

import local.kongyu.netty.netty.NettyClientService;
import local.kongyu.netty.netty.NettyServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageController {
    private final NettyClientService nettyClientService;

    @GetMapping("/test")
    public String test() {
        return "ok";
    }

    @GetMapping("/server")
    public String server(String message) {
        NettyServerService.send(message);
        return "ok";
    }

    @GetMapping("/client")
    public String client(String message) {
        nettyClientService.send(message);
        return "ok";
    }

}

