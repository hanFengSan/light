package com.yakami.light;

import android.content.SharedPreferences;

import com.yakami.light.bean.RawTimeRankContainer;
import com.yakami.light.bean.Version;
import com.yakami.light.model.SharePreferenceManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;


public class ServerAPI {

    public final static String animeDiscsHost = "http://www.anime-discs.com/";
    public final static String baiduHost = "http://www.baidu.com";
    public final static String lightHost = "http://112.74.204.89";

    private static SakuraAPI mSakuraAPI;
    private static BangumiInfoAPI mBangumiInfoAPI;
    private static LightAPI mLightAPI;

    private static String mCookies;
    private static String mSessionCookies;

    public static SakuraAPI getSakuraAPI() {
        if (mSakuraAPI == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ServerAPI.animeDiscsHost)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            mSakuraAPI = retrofit.create(SakuraAPI.class);
        }
        return mSakuraAPI;
    }

    public static BangumiInfoAPI getBangumiInfoAPI() {
        if (mBangumiInfoAPI == null) {
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor((Interceptor.Chain chain) -> {
                                Request original = chain.request();
                                String path = original.url().url().getPath();

//                                if (path.contains("lain.bgm.tv")) {
                                Request.Builder requestBuilder = original.newBuilder()
                                        .header("Connection", "close")
                                        .method(original.method(), original.body());
                                Request request = requestBuilder.build();
                                return chain.proceed(request);
//                                }
//                                switch (original.url().url().getPath()) {
//                                    case "/User/Login":
//                                        Response response = chain.proceed(chain.request());
//                                        setCookies(response.header("Set-Cookie"));
//                                        return response;
//                                    case "/User/GetValidateCode":
//                                        Response codeResponse = chain.proceed(chain.request());
//                                        mSessionCookies = codeResponse.header("Set-Cookie");
//                                        return codeResponse;
//                                    case "/User/SignUp":
//                                        Request.Builder signUpRequestBuilder = original.newBuilder()
//                                                .header("Cookie", mSessionCookies == null ? "" : mSessionCookies)
//                                                .method(original.method(), original.body());
//                                        Request signUpRequest = signUpRequestBuilder.build();
//                                        return chain.proceed(signUpRequest);
//                                    default:
//                                        Request.Builder requestBuilder = original.newBuilder()
//                                                .header("Cookie", mCookies == null ? getCookies() : mCookies)
//                                                .method(original.method(), original.body());
//                                        Request request = requestBuilder.build();
//                                        return chain.proceed(request);
//                                }
//                                if (original.url().url().getPath().equals("/User/Login")) {
//                                    Response response = chain.proceed(chain.request());
//                                    setCookies(response.header("Set-Cookie"));
//                                    return response;
//                                } else {
//                                    Request.Builder requestBuilder = original.newBuilder()
//                                            .header("Cookie", mCookies == null ? getCookies() : mCookies)
//                                            .method(original.method(), original.body());
//                                    Request request = requestBuilder.build();
//                                    return chain.proceed(request);
//                                }
                            }
                    ).build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baiduHost)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();
            mBangumiInfoAPI = retrofit.create(BangumiInfoAPI.class);
        }
        return mBangumiInfoAPI;
    }

    public static LightAPI getLightAPI() {
        if (mLightAPI == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(lightHost)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            mLightAPI = retrofit.create(LightAPI.class);
        }
        return mLightAPI;
    }

    public static void setCookies(String cookies) {
        mCookies = cookies;
        SharedPreferences.Editor editor = SharePreferenceManager.getUser().edit();
        editor.putString(SharePreferenceManager.User.KEY_COOKIES, cookies);
        editor.apply();
        clearCookies();
    }


    public static String getCookies() {
        if (mCookies == null) {
            SharedPreferences preferences = SharePreferenceManager.getUser();
            return mCookies = preferences.getString(SharePreferenceManager.User.KEY_COOKIES, "");
        }
        return mCookies;
    }

    public static void clearCookies() {
        mCookies = null;
    }

    public static String getPreviewImgUrl(String url) {
        try {
            return ServerAPI.animeDiscsHost + "/Home/GetArticleImg" + "?url=" + URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String getValidateCode() {
        return ServerAPI.animeDiscsHost + "/User/GetValidateCode";
    }

    public static String getUploadsUrl(String url) {
        return ServerAPI.animeDiscsHost + "/Uploads/" + url;
    }

    public interface SakuraAPI {

        @GET("/sakura.do")
        Observable<List<RawTimeRankContainer>> getData();

    }

    public interface BangumiInfoAPI {

        /**
         * 百度搜索接口
         *
         * @param codingType 一般为utf-8
         * @param keyword
         * @return
         */
        @GET("/s")
        Observable<ResponseBody> getSearchResult(@Query("ie") String codingType,
                                                 @Query("wd") String keyword);

        @GET
        Observable<ResponseBody> getBGMResult(@Url String url);

        @GET
        @Streaming
        Observable<ResponseBody> getImage(@Url String url);

    }

    public interface LightAPI {

        @GET("/Uploads/checkVersion.txt")
        Observable<Version> checkUpdate();
    }

}
