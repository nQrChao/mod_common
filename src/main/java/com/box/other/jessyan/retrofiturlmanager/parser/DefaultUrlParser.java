/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.box.other.jessyan.retrofiturlmanager.parser;

import com.box.other.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.HttpUrl;

import static com.box.other.jessyan.retrofiturlmanager.RetrofitUrlManager.IDENTIFICATION_PATH_SIZE;

public class DefaultUrlParser implements UrlParser {

    private UrlParser mDomainUrlParser;
    private volatile UrlParser mAdvancedUrlParser;
    private volatile UrlParser mSuperUrlParser;
    private RetrofitUrlManager mRetrofitUrlManager;

    @Override
    public void init(RetrofitUrlManager retrofitUrlManager) {
        this.mRetrofitUrlManager = retrofitUrlManager;
        this.mDomainUrlParser = new DomainUrlParser();
        this.mDomainUrlParser.init(retrofitUrlManager);
    }

    @Override
    public HttpUrl parseUrl(HttpUrl domainUrl, HttpUrl url) {
        if (null == domainUrl) return url;

        if (url.toString().contains(IDENTIFICATION_PATH_SIZE)) {
            if (mSuperUrlParser == null) {
                synchronized (this) {
                    if (mSuperUrlParser == null) {
                        mSuperUrlParser = new SuperUrlParser();
                        mSuperUrlParser.init(mRetrofitUrlManager);
                    }
                }
            }
            return mSuperUrlParser.parseUrl(domainUrl, url);
        }

        //如果是高级模式则使用高级解析器
        if (mRetrofitUrlManager.isAdvancedModel()) {
            if (mAdvancedUrlParser == null) {
                synchronized (this) {
                    if (mAdvancedUrlParser == null) {
                        mAdvancedUrlParser = new AdvancedUrlParser();
                        mAdvancedUrlParser.init(mRetrofitUrlManager);
                    }
                }
            }
            return mAdvancedUrlParser.parseUrl(domainUrl, url);
        }
        return mDomainUrlParser.parseUrl(domainUrl, url);
    }
}
