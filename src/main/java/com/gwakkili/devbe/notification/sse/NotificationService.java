package com.gwakkili.devbe.notification.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;
    public SseEmitter connect(long memberId) {
        SseEmitter emitter = createEmitter(memberId);

        sendToClient(memberId, "EventStream Created. [memberId = "+ memberId + "]");

        return emitter;
    }

    private void sendToClient(long memberId, Object data) {
        SseEmitter emitter = emitterRepository.get(memberId);

        if(emitter != null){
            try {
                emitter.send(SseEmitter.event().id(String.valueOf(memberId)).data(data));
            } catch (IOException e) {
                emitterRepository.deleteById(memberId);
                emitter.completeWithError(e);
            }
        }

    }

    public void notify(Long memberId, Object event){
        sendToClient(memberId, event);
    }

    private SseEmitter createEmitter(long memberId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(memberId, emitter);

        emitter.onCompletion(()-> emitterRepository.deleteById(memberId));
        emitter.onTimeout(() -> emitterRepository.deleteById(memberId));

        return emitter;
    }
}
