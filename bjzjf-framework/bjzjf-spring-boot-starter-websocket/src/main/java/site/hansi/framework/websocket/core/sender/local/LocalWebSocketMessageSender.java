package site.hansi.framework.websocket.core.sender.local;

import site.hansi.framework.websocket.core.sender.AbstractWebSocketMessageSender;
import site.hansi.framework.websocket.core.sender.WebSocketMessageSender;
import site.hansi.framework.websocket.core.session.WebSocketSessionManager;

/**
 * 本地的 {@link WebSocketMessageSender} 实现类
 *
 * 注意：仅仅适合单机场景！！！
 *
 * @author 北京智匠坊
 */
public class LocalWebSocketMessageSender extends AbstractWebSocketMessageSender {

    public LocalWebSocketMessageSender(WebSocketSessionManager sessionManager) {
        super(sessionManager);
    }

}
