import http from "../http-common.js";

class FetchDataService {

    getAllWorkers() {
        return http.get("/workers");
    }

    createWorker(workerData) {
        return http.post("/auth/register/worker", workerData);
    }

    createUser(userData) {
        return http.post("/auth/register/user", userData);
    }
    getWorkerById(sid){
        return http.get(`/workers/${sid}`)
    }

    updateWorker(sid, data){
        return  http.put(`/workers/${sid}`, data)
    }
    
    getUserById(sid){
        return http.get(`/users/${sid}`)
    }
    
    getAllJobs() {
        return http.get("/jobs"); 
    }

    getjobBySkill(skill){
        return http.get(`/jobs/skills`, { params: { skills: [skill] } })
    }

    getjobByWorkerId(workerId){
        return http.get(`/jobs/worker/${workerId}`)
    }

    completeJob(jobId) {
        return http.post(`/jobs/${jobId}/complete`);
    }

    setJobRating(jobId, rating) {
        return http.post(`/jobs/${jobId}/rate?rating=${rating}`);
    }

    getJobByUserId(userId){
        return http.get(`/jobs/user/${userId}`);
    }

    postNewJob(clientId, data){
        return http.post(`/jobs`, data)
    }

    updateJobs(sid, data){
        return http.put(`/jobs/${sid}`, data)
    }

    //endpoints for message
    getAllUserMessages(userId) {
        return http.get(`/messages/user/${userId}`);
    }

    getAllWorkerMessages(workerId) {
        return http.get(`/messages/worker/${workerId}`);
    }

    sendMessage(messageData) {
        return http.post(`/messages/send`, messageData);
    }

    searchWorkers(query) {
        return http.get(`/workers/search`, { params: { keyword: query } });
    }

    // Additional job endpoints
    getJobsBySkills(skills) {
        return http.get(`/jobs/skills`, { params: { skills: skills } });
    }

    getJobsByBudgetRange(minBudget, maxBudget) {
        return http.get(`/jobs/budget`, { params: { minBudget, maxBudget } });
    }

    getJobsByLocation(location) {
        return http.get(`/jobs/location/${location}`);
    }

    searchJobs(keyword) {
        return http.get(`/jobs/search`, { params: { keyword } });
    }

    getRecentJobs(limit = 10) {
        return http.get(`/jobs/recent`, { params: { limit } });
    }

    getCompletedJobs(userId) {
        return http.get(`/jobs/user/${userId}/completed`);
    }

    getPendingJobs(userId) {
        return http.get(`/jobs/user/${userId}/pending`);
    }

    // Additional worker endpoints
    getWorkersBySkill(skill) {
        return http.get(`/workers/skills/${skill}`);
    }

    getWorkersByRating(rating) {
        return http.get(`/workers/rating/${rating}`);
    }

    getWorkersByLocation(location) {
        return http.get(`/workers/location/${location}`);
    }

    rateWorker(workerId, rating) {
        return http.post(`/workers/${workerId}/rate`, null, { params: { rating } });
    }

    getWorkerJobs(workerId) {
        return http.get(`/workers/${workerId}/jobs`);
    }

    getWorkerContactInfo(workerId) {
        return http.get(`/workers/${workerId}/contact`);
    }

    getWorkerWorkingHours(workerId) {
        return http.get(`/workers/${workerId}/working-hours`);
    }

    // Additional message endpoints
    getConversation(userId, workerId) {
        return http.get(`/messages/conversation`, { params: { userId, workerId } });
    }

    getRecentMessages(userId, workerId, limit = 20) {
        return http.get(`/messages/recent`, { params: { userId, workerId, limit } });
    }

    searchMessages(userId, workerId, keyword) {
        return http.get(`/messages/search`, { params: { userId, workerId, keyword } });
    }

    deleteMessage(messageId) {
        return http.delete(`/messages/${messageId}`);
    }
    
}

export default new FetchDataService();
