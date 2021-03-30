package com.dsciiita.inclusivo.api;

import com.dsciiita.inclusivo.models.Company;
import com.dsciiita.inclusivo.models.Initiative;
import com.dsciiita.inclusivo.models.Job;
import com.dsciiita.inclusivo.models.JobFilterBody;
import com.dsciiita.inclusivo.models.Scholarship;
import com.dsciiita.inclusivo.models.Story;
import com.dsciiita.inclusivo.models.User;
import com.dsciiita.inclusivo.models.UserCandidate;
import com.dsciiita.inclusivo.models.UserEmployee;
import com.dsciiita.inclusivo.responses.ApplicationByCandidate;
import com.dsciiita.inclusivo.responses.ApplicationByIdResponse;
import com.dsciiita.inclusivo.responses.ApplicationsByJobResponse;
import com.dsciiita.inclusivo.responses.CompanyInitiativesResponse;
import com.dsciiita.inclusivo.responses.CompanyJobsResponse;
import com.dsciiita.inclusivo.responses.CompanyListsResponse;
import com.dsciiita.inclusivo.responses.CompanyResponse;
import com.dsciiita.inclusivo.responses.CompanyScholarshipResponse;
import com.dsciiita.inclusivo.responses.CompanyStoryResponse;
import com.dsciiita.inclusivo.responses.DefaultResponse;
import com.dsciiita.inclusivo.responses.DegreeListsResponse;
import com.dsciiita.inclusivo.responses.EmailResponse;
import com.dsciiita.inclusivo.responses.EvaluationResponse;
import com.dsciiita.inclusivo.responses.FilterCompanyResponse;
import com.dsciiita.inclusivo.responses.FilterJobResponse;
import com.dsciiita.inclusivo.responses.FilterScholarshipsResponse;
import com.dsciiita.inclusivo.responses.FollowedCompaniesResponse;
import com.dsciiita.inclusivo.responses.GetUserResponse;
import com.dsciiita.inclusivo.responses.IDStoryResponse;
import com.dsciiita.inclusivo.responses.InitiativeByIdResponse;
import com.dsciiita.inclusivo.responses.JobByIdResponse;
import com.dsciiita.inclusivo.responses.LikedJobsResponse;
import com.dsciiita.inclusivo.responses.LikedScholarshipResponse;
import com.dsciiita.inclusivo.responses.LocationResponse;
import com.dsciiita.inclusivo.responses.LoginResponse;
import com.dsciiita.inclusivo.responses.ScholarshipByIDResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface UserService {


        //auth calls
        @FormUrlEncoded
        @POST("/rest-auth/registration/")
        Call<DefaultResponse> registerUser(@Field("email") String email,
                                           @Field("password1") String pw1,
                                           @Field("password2") String pw2);

        @FormUrlEncoded
        @POST("/rest-auth/login/")
        Call<LoginResponse> loginUser(@Field("email") String email,
                                      @Field("password") String pw);



        //common user details
        @POST("/user/update/")
        Call<User> updateUser(@Body User user,
                              @Header("Authorization") String Token);

        @GET("/user/get/0")
        Call<GetUserResponse> getUser(@Header("Authorization") String token);


        //complete profiles
        @POST("/user/complete_profile/")
        Call<UserCandidate> completeCandidate(@Body UserCandidate candidate, @Header("Authorization") String Token);

        @POST("/user/complete_profile/")
        Call<UserEmployee> completeEmployee(@Body UserEmployee employee, @Header("Authorization") String Token);



        //user api
        @FormUrlEncoded
        @POST("/user/check/user/")
        Call<EmailResponse> checkUser(@Field("email") String email);



        //company apis
        @POST("/company/add/")
        Call<DefaultResponse> addCompany(@Body Company company, @Header("Authorization") String Token);

        @POST("/company/get/")
        Call<FilterCompanyResponse> getCompanyFilter(@Body JobFilterBody jobFilterBody,
                                                     @Header("Authorization") String Token);

        @GET("/company/dropdown")
        Call<CompanyListsResponse> getCompanies(@Header("Authorization") String Token);

        @GET("/company/company_details/{COMPANY_ID}")
        Call<CompanyResponse> getCompany(@Path("COMPANY_ID") int companyId, @Header("Authorization") String Token);

        @POST("/company/follow/{COMPANY_ID}/")
        Call<DefaultResponse> followCompany(@Path("COMPANY_ID") int id, @Header("Authorization") String Token);

        @POST("/company/unfollow/{COMPANY_ID}/")
        Call<DefaultResponse> unfollowCompany(@Path("COMPANY_ID") int id, @Header("Authorization") String Token);

        @GET("/dashboard/get/subscribed_companies")
        Call<FollowedCompaniesResponse> getFollowedCompanies(@Header("Authorization") String Token);


        //general lists api
        @GET("/user/degree/dropdown")
        Call<DegreeListsResponse> getDegrees();

        @GET("/user/location/dropdown/{COUNTRY}")
        Call<LocationResponse> getLocations(@Path("COUNTRY") String country);


        //job apis
        @POST("/job/add/")
        Call<DefaultResponse> addJob(@Body Job job, @Header("Authorization") String Token);

        @FormUrlEncoded
        @POST("/job/status/update/{JOB_ID}/")
        Call<DefaultResponse> updateJobStatus(@Path("JOB_ID") int jobId,
                                           @Field("status") String status,
                                           @Header("Authorization") String Token);

        @GET("/job/company/{COMPANY_ID}")
        Call<CompanyJobsResponse> getJobsForCompany(@Path("COMPANY_ID") int id, @Header("Authorization") String Token);

        @GET("/job/get/{JOB_ID}")
        Call<JobByIdResponse> getJobByID(@Path("JOB_ID") int id, @Header("Authorization") String Token);

        @POST("/job/get/")
        Call<FilterJobResponse> getJobsByFilter(@Body JobFilterBody jobFilterBody,
                                                @Header("Authorization") String Token);

        @GET("/job/candidate/")
        Call<ApplicationByCandidate> getAppliedJobs(@Header("Authorization") String Token);

        @GET("/dashboard/get/jobs_location/?=")
        Call<CompanyJobsResponse> getJobsByPrefCity(@Header("Authorization") String Token);

        @GET("/dashboard/get/liked_jobs")
        Call<LikedJobsResponse> getLikedJobs(@Header("Authorization") String Token);

        @POST("/job/like/{JOB_ID}/")
        Call<DefaultResponse> likeJob(@Path("JOB_ID") int jobId, @Header("Authorization") String Token);

        @POST("/job/unlike/{JOB_ID}/")
        Call<DefaultResponse> unlikeJob(@Path("JOB_ID") int jobId, @Header("Authorization") String Token);

        @GET("/job/get/by_diversity/")
        Call<CompanyJobsResponse> getJobsByTags(@QueryMap Map<String, String> param, @Header("Authorization") String Token);



        //initiative calls
        @POST("/company/initiatives/add/")
        Call<DefaultResponse> addInitiative(@Body Initiative initiative, @Header("Authorization") String Token);

        @GET("/company/initiatives/get_by_company/{COMPANY_ID}")
        Call<CompanyInitiativesResponse> getInitiativeByCompany(@Path("COMPANY_ID") int companyId, @Header("Authorization") String Token);

        @GET("/company/initiatives/get/{INITIATIVE_ID}")
        Call<InitiativeByIdResponse> getInitiativeByID(@Path("INITIATIVE_ID") int initiativeId, @Header("Authorization") String Token);

        @DELETE("/company/initiatives/delete/{INITIATIVE_ID}/")
        Call<Void> deleteInitiative(@Path("INITIATIVE_ID") int initiativeId, @Header("Authorization") String Token);



        //story calls
        @POST("/company/story/add/")
        Call<DefaultResponse> addStory(@Body Story story, @Header("Authorization") String Token);

        @GET("/company/story/get/")
        Call<CompanyStoryResponse> getStoryByCompany(@Query("company_id") int companyID,
                                                     @Header("Authorization") String Token);

        @GET("/company/story/get/")
        Call<CompanyStoryResponse> getAllStories(@Header("Authorization") String Token);

        @GET("/company/story/get/{STORY_ID}/individual")
        Call<IDStoryResponse> getStoryByID(@Path("STORY_ID") int id, @Header("Authorization") String Token);

        @DELETE("/company/story/delete/{STORY_ID}/")
        Call<Void> deleteStory(@Path("STORY_ID") int initiativeId, @Header("Authorization") String Token);



        //job applications
        @FormUrlEncoded
        @POST("/job/application/create/{JOB_ID}/")
        Call<DefaultResponse> addApplicationForJob(@Path("JOB_ID") int id, @Field("job_post_id") int jobid,
                                                   @Field("status") String status,
                                                   @Header("Authorization") String Token,
                                                   @Field("message") String message);

        @GET("/job/applications/{JOB_ID}/list")
        Call<ApplicationsByJobResponse> getApplicationsForJob(@Path("JOB_ID") int id, @Header("Authorization") String Token);

        @FormUrlEncoded
        @POST("/job/application/status/update/{APPLICATION_ID}/")
        Call<DefaultResponse> updateApplicationStatus(@Path("APPLICATION_ID") int applicationId,
                                              @Field("status") String status,
                                              @Header("Authorization") String Token);

        @GET("/job/application/get/{APPLICATION_ID}")
        Call<ApplicationByIdResponse> getApplicationByID(@Path("APPLICATION_ID") int id, @Header("Authorization") String Token);


        @POST("/job/application/evaluate/{JOB_ID}/")
        Call<EvaluationResponse> evaluateCandidate(@Path("JOB_ID") int id, @Header("Authorization") String Token);


        //scholarships
        @POST("/job/scholarship/get/")
        Call<FilterScholarshipsResponse> getScholarships(@Body JobFilterBody jobFilterBody,
                                                         @Header("Authorization") String Token);

        @POST("/job/scholarship/like/{SCHOLARSHIP_ID}/")
        Call<DefaultResponse> likeScholarship(@Path("SCHOLARSHIP_ID") int jobId, @Header("Authorization") String Token);

        @POST("/job/scholarship/unlike/{SCHOLARSHIP_ID}/")
        Call<DefaultResponse> unlikeScholarship(@Path("SCHOLARSHIP_ID") int jobId, @Header("Authorization") String Token);

        @GET("/job/scholarship/get/{JOB_ID}")
        Call<ScholarshipByIDResponse> getScholarshipById(@Path("JOB_ID") int id, @Header("Authorization") String Token);

        @GET("/dashboard/get/liked_scholarships")
        Call<LikedScholarshipResponse> getLikedScholarships(@Header("Authorization") String Token);

        @POST("/job/scholarship/add/")
        Call<DefaultResponse> addScholarship(@Body Scholarship job, @Header("Authorization") String Token);

        @GET("/job/scholarship/company/{COMPANY_ID}")
        Call<CompanyScholarshipResponse> getScholarshipsByCompany(@Path("COMPANY_ID") int companyID,
                                                              @Header("Authorization") String Token);
}
