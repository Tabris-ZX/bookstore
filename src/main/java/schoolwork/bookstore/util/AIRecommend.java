package schoolwork.bookstore.util;

import org.springframework.stereotype.Component;
import schoolwork.bookstore.config.AIConfig;

@Component
public class AIRecommend {
    AIConfig aiConfig;
    public AIRecommend(AIConfig aiConfig) {
        this.aiConfig = aiConfig;
    }


}
