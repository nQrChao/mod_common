package com.box.common.network;

import com.box.common.data.model.AiResult;

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
