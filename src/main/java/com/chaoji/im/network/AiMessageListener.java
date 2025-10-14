package com.chaoji.im.network;

import com.chaoji.im.data.model.AiResult;

public interface AiMessageListener {
    /**
     * 错误消息回执
     */
    void onErrorReceipt(AiResult aiResult);

    /**
     * 问题回执
     */
    void onQuestionReceipt(AiResult aiResult);

    /**
     * 答案回执
     */
    void onAnswerReceipt(AiResult AiResult);

}
