package com.appshroom.leanpocket.api.retrofit;

import com.appshroom.leanpocket.dto.GetHostNamesRequestBody;
import com.appshroom.leanpocket.dto.LeanKitResponse;
import com.appshroom.leanpocket.dto.OrganizationLink;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by jpetrakovich on 8/19/13.
 */
public interface RetroLeanKitNonPublicApi {

    @POST("/GetHostnames")
    public void getHostNames(@Body GetHostNamesRequestBody getHostNamesRequest,
                             Callback<LeanKitResponse<List<OrganizationLink>>> cb);

}
