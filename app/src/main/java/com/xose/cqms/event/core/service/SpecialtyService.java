package com.xose.cqms.event.core.service;

import com.xose.cqms.event.core.Constants;
import com.xose.cqms.event.core.modal.ApiResponse;
import com.xose.cqms.event.core.modal.Specialty;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Josekutty on 7/13/2016.
 */
public interface SpecialtyService {

    @GET(Constants.Http.URL_IMPORT_SPECIALTY)
    Call<ApiResponse<Specialty>> getSpecialty();
}
