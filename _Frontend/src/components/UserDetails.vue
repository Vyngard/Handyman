<template>
    <div class="container-cards">
        
        <div class="cards-description">
           
            <div class ="personal-data">
                <img src="images/people.png" class="profile" />
                <p class="data-description"><strong>Name:</strong> {{ this.fullName || user.username }}</p>
                <p class="data-description"><strong>Email:</strong> {{ user.email }}</p>
                <p class="data-description"><strong>User ID:</strong> {{ user.id }}</p>
                <p class="data-description" v-if="user.credit !== undefined"><strong>Credit:</strong> ${{ user.credit }}</p>
                <p class="data-description" v-if="user.phone"><strong>Phone:</strong> {{ user.phone }}</p>
                <p class="data-description" v-if="user.location"><strong>Location:</strong> {{ user.location }}</p>
                <p class="data-description" v-if="user.createdAt"><strong>Member Since:</strong> {{ formatDate(user.createdAt) }}</p>
            </div>
            <div class ="container-form">
                <div class="ctn-title">
                    <h2 class=" title ">Post a Job</h2>
                </div>
                <form @submit.prevent="submitForm">
                    <div class="container-lbl">

                      <select v-model="newJob.title" required placeholder="Title" >
                        <option value="" disabled>Select category</option>
                        <option value="Carpentry">Carpentry</option>
                        <option value="Plumbing">Plumbing</option>
                        <option value="Masonry">Masonry</option>
                        <option value="Gardening">Gardening</option>
                        <option value="Electrical">Electrical</option>
                      </select>
                      <!-- <input type="text" id="email" v-model="newJob.title" required="true" placeholder="Title"/> -->
                        
                    </div>
                    <div class="container-lbl">
                     
                        <textarea v-model="newJob.description" required="true" placeholder="Description of the job"></textarea>
                        
                    </div>
                    <div class="container-lbl">
                      
                        <input type="text" id="budget" v-model="newJob.budget" required="true" placeholder ="Budget" />
                        <span v-if="!isValidBudget" class="error-message">{{ errorMessage2 }}</span>
                        
                    </div>
                    <div class=" container-btn">
                        <button type="submit" @click = "handleSubmit">Send</button>
                        <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>

                    </div>
                </form>
            </div>          
        </div>
    <div class="card-jobs">   

      <div class="title-jobs">       
        
        <button @click="markAsUnCompleted" class="fetchData">Recent Jobs</button>
        <button @click="markAsCompleted" class="fetchData">Completed Jobs</button>
       

      </div>
      
      <div v-if="completedJobs && completedJobs.length > 0">
        <div v-for="(job, index) in completedJobs" :key="index" class="job">
          <p class="desc-job" style="font-weight: bold;">{{ job.title }}</p>
          <p class="desc-job"><strong>Description:</strong> {{ job.description }}</p>
          <p class="desc-job" v-if="job.worker"><strong>Worker:</strong> {{ job.worker.username }}</p>
          <p class="desc-job" v-if="job.location"><strong>Location:</strong> {{ job.location }}</p>
          <p class="desc-job" v-if="job.skills && job.skills.length > 0">
            <strong>Skills:</strong> {{ job.skills.map(skill => skill.name).join(', ') }}
          </p>
          <p class="desc-job"><strong>Budget:</strong> ${{ job.budget }}</p>
          <p class="desc-job"><strong>Job ID:</strong> {{ job.id }}</p>
          <p class="desc-job" v-if="job.createdAt"><strong>Created:</strong> {{ formatDate(job.createdAt) }}</p>
          <p class="desc-job" v-if="job.completedAt"><strong>Completed:</strong> {{ formatDate(job.completedAt) }}</p>
          <p class="desc-job">
            <strong>Rating:</strong>
            <star-rating v-model:rating="job.rating"
              star-size="35"
              show-rating=false
              animate=true
              @update:rating="setRating(job.id, $event)">
            </star-rating>
          </p>
        </div>
      </div>
      
      <div v-if="!completedJobs || completedJobs.length === 0">
        <div class="no-jobs-message">
          <p>No completed jobs yet.</p>
        </div>
      </div>
  
      <div v-if="uncompletedJobs && uncompletedJobs.length > 0">
        <div v-for="(job, index) in uncompletedJobs" :key="index" class="job">
          <p class="desc-job" style="font-weight: bold;">{{ job.title }}</p>
          <p class="desc-job"><strong>Description:</strong> {{ job.description }}</p>
          <p class="desc-job" v-if="job.worker"><strong>Assigned Worker:</strong> {{ job.worker.username }}</p>
          <p class="desc-job" v-else><strong>Status:</strong> <span style="color: orange;">Looking for worker</span></p>
          <p class="desc-job" v-if="job.location"><strong>Location:</strong> {{ job.location }}</p>
          <p class="desc-job" v-if="job.skills && job.skills.length > 0">
            <strong>Skills Required:</strong> {{ job.skills.map(skill => skill.name).join(', ') }}
          </p>
          <p class="desc-job"><strong>Budget:</strong> ${{ job.budget }}</p>
          <p class="desc-job"><strong>Job ID:</strong> {{ job.id }}</p>
          <p class="desc-job" v-if="job.createdAt"><strong>Posted:</strong> {{ formatDate(job.createdAt) }}</p>
          <p class="desc-job" v-if="job.rating !== null && job.rating !== undefined">
            <strong>Current Rating:</strong>
            <star-rating v-model:rating="job.rating"
              star-size="35"
              show-rating=false
              animate=true
              @update:rating="setRating(job.id, $event)">
            </star-rating>
          </p>
          <p class="desc-job">
            <button @click="markCompleted(job.id)" v-if="job.worker != null" class="button-profile">Mark as Completed</button>
          </p>
        </div>
      </div>
    </div>
    
    <div v-if="!uncompletedJobs || uncompletedJobs.length === 0">
      <div class="no-jobs-message">
        <p>No pending jobs. Create a new job above to get started!</p>
      </div>
    </div>
  </div>
</template>
  
<script>
  import FetchDataServices from '../services/FetchDataService'
  import StarRating from 'vue-star-rating';

  //import HeaderComponent from './HeaderComponent.vue'
  
  export default {
    name: "userDetails",

    components: {
      StarRating,
      //HeaderComponent,
    },

    data() {
      return {
        newLogin: false,
        user: {
          username: "",
          email: "",
          credit: 0,
          fullName: ""
        },
        jobs: [],
        date: "",
        random_number: 0,
        completedJobs: [],
        uncompletedJobs: [],
        clientId: null,
        newJob: {
          title:"",
          description:"",
          budget: ""
        }, 
        isValidBudget: true,
        errorMessage :"",
        errorMessage2 :"",
        rating: "Rating not available",
        message: "We are seeking a professional for ",
        message2: ". It's required knowledge of construction materials and carpentry techniques. Ability to interpret blueprints and follow instructions. Teamwork skills and ability to meet deadlines"
      }

    },

    methods: {
        retrieveUser() {
          let id = localStorage.getItem('userId')
          let fullName ;
          this.fullName = localStorage.getItem('fullName')
          console.log('Full name:', fullName);
        
          console.log("User ID:", id)
          
          if (!id) {
            console.error('No user ID found in localStorage');
            return;
          }
          
          FetchDataServices.getUserById(id)
            .then(response => {
              console.log('User data:', response.data);
              this.user = response.data;
              this.fetchJobs(id);
            })
            .catch(error => {
              console.error('Error fetching user:', error);
              if (error.response) {
                console.log('Error response:', error.response.data);
                console.log('Error status:', error.response.status);
              }
            })
        },
        fetchJobs(id) {
          console.log('Fetching jobs for user:', id);
          FetchDataServices.getJobByUserId(id)
            .then(response => {
              console.log('Jobs response:', response.data);
              // Ensure jobs is always an array
              this.jobs = Array.isArray(response.data) ? response.data : [];
              this.markAsUnCompleted();
            })
            .catch(error => {
              console.error('Error fetching jobs:', error);
              // Initialize as empty array on error
              this.jobs = [];
              this.completedJobs = [];
              this.uncompletedJobs = [];
              
              if (error.response) {
                console.log('Error response:', error.response.data);
                console.log('Error status:', error.response.status);
              }
            });
        },
        markAsCompleted() {
          // Ensure jobs is an array before filtering
          if (!Array.isArray(this.jobs)) {
            console.warn('Jobs is not an array:', this.jobs);
            this.jobs = [];
          }
          this.completedJobs = this.jobs.filter(job => job.isCompleted);
          this.uncompletedJobs = [];
        },
        markAsUnCompleted() {
          // Ensure jobs is an array before filtering
          if (!Array.isArray(this.jobs)) {
            console.warn('Jobs is not an array:', this.jobs);
            this.jobs = [];
          }
          this.uncompletedJobs = this.jobs.filter(job => !job.isCompleted);
          this.completedJobs = [];
          console.log('Uncompleted jobs:', this.uncompletedJobs);
        },
        markCompleted(jobId) {
          FetchDataServices.completeJob(jobId)
            .then(response => {
              console.log("Job marked as completed:", jobId);
              console.log(response);
              this.fetchJobs(localStorage.getItem('userId'));
            })
            .catch(error => {
              console.error("Error marking job as completed:", error);
            });
        },
        checklogin() {
          console.log("")
          localStorage.setItem('newLogin', false);
          this.newLogin = localStorage.getItem('newLogin');
          console.log(this.newLogin)
          window.location.reload();
        } ,

        handleSubmit(){
          let id = localStorage.getItem('userId')
          //input validation 
          if (this.newJob.title.trim() === '' || this.newJob.description.trim() === '' || this.newJob.budget.trim() === '') {
            this.errorMessage = "All fields are required";
            return; 
           }

    
          if (isNaN(this.newJob.budget)) {
              this.isValidBudget = false;
              this.errorMessage2 = "Budget must be a number";
              return; 
          }
          let newJobPost = {
              title: this.newJob.title,
              description: this.newJob.description,
              budget: parseFloat(this.newJob.budget)
          };
      
          console.log('Creating new job:', newJobPost);
          
          FetchDataServices.postNewJob(id, newJobPost)
          .then(response=>{
            console.log('Job created successfully:', response);
            this.fetchJobs(id);
            // Clear form
            this.newJob.title = "";
            this.newJob.description = "";
            this.newJob.budget = "";
            this.errorMessage = "";
            this.errorMessage2 = "";
            this.isValidBudget = true;
          })
          .catch(error => {
              console.error('Error creating job:', error);
              if (error.response) {
                console.log('Error response:', error.response.data);
                console.log('Error status:', error.response.status);
                this.errorMessage = error.response.data.message || "Error creating job. Please try again.";
              } else {
                this.errorMessage = "Network error. Please try again.";
              }
            });

          }, 
        
      setRating(jobId, rating) {
            FetchDataServices.setJobRating(jobId, rating)
          .then(response => {
            console.log("Job rating updated:", jobId, "Rating:", rating);
            console.log(response)
          })
          .catch(error => {
            console.error("Error updating job rating:", error);
          });
          },

        formatDate(dateString) {
          if (!dateString) return 'N/A';
          return new Date(dateString).toLocaleDateString();
        }
        
      },
      mounted() {
        this.retrieveUser(); 
        let newLogin = localStorage.getItem('newLogin'); 
        console.log("workerdetails newlogin:" + newLogin);   
        if (newLogin === 'true') { // Check if newLogin is true
          localStorage.setItem('newLogin', false);
          this.checklogin();
        }  
      }, 
   
  }
</script>
  
<style scoped >

  .container-cards{
      width: 95%;
      margin: auto;
      overflow: auto;
      /*background-color: aqua;*/
      display: flex;
  }
  
  .input-search{
      margin: auto;
     
      text-align: left;
      margin-bottom: 25px;
  }
  input{
      width: 80%;
      height: 30px;
      border-radius: 0.8rem;
      margin-left: 25px;
      background-size: 20px; 
      background-repeat: no-repeat;
      margin-top: 20px;
      
  }
  .profile{
      display: block;
      margin-top: 25px;
      height: 150px;
      width: 150px;
      
  }
  .cards-description{
    text-align: center;
    width: 20%;
    height: 850px;
    align-items: flex-start;
    border-radius: 0.5rem;
    
  
  }
  
  .card-jobs{
      width: 70%;
      text-align: center;
      margin-left: 100px;  
  
  }
  
  
  .job{
      margin-left: 20px;
      margin-bottom: 20px;
      width: 80%;
      border-radius: 0.5rem;
      text-align: left;
      background-color: rgb(229, 236, 238);
      padding: 5px;
      font-size: 20px;      
  }
  
  .desc-job{
      margin-left: 30px;
      font-size: 18px;
  }
  
  .cards{   
    background-color: rgb(121, 168, 194);
    align-items: flex-start;    
  }

  .personal-data{
     
      background-color: rgb(230, 239, 247);
      border-radius: 0.5rem;
      padding: 5px;
      font-size: 18px;
      margin-top: 15px;
  }

  .data-description{
      margin-bottom: 15px;
      padding: 2px;
      font-size: 20px;     
  }

  .setting{
    height: 350px;
    margin-bottom: 50px;
    background-color: rgb(230, 239, 247);
    border-radius: 0.5rem;
    margin-top: 15px;
    padding: 5px;
    font-size: 20px;
    text-align: left;  
  }
  
  
  .title-jobs{
    text-align: left;
  }

  .fetchData{
    margin-left: 30px;
    margin-bottom: 40px;
    font-size: 18px;
    background-color: transparent;
    border: none;
    color: rgb(60, 172, 15); 
    cursor: pointer;
    font-size: inherit;
    padding: 0; 
    font-weight: bold;
    font-size: 25px;
  }
  .edit{
      width: 30px;
      height: 40px;
  }
  .edit-preferences{
      display: flex
  }
  h2{
    margin-top: 15px;
    font-size: 25px;
    width: 90%;
    text-align: center;
  }
  
  .desc-job{
    font-size: 20px;
  }
  .desc-jobs1{
    font-size: 20px;
    text-align: right;
    margin-right: 15px;
    color:rgb(85, 80, 76)
  
  }
  .container-apply-btn{
    text-align: right;
  }
  .apply-btn{
    margin-left: 30px;
    margin-bottom: 20px;
    font-size: 18px;
    background-color: transparent;
    border: none;
    color: rgb(214, 132, 65); 
    cursor: pointer;
    font-size: inherit;
    padding: 0; 
    font-weight: bold;
    font-size: 20px;
    margin-right: 15px;       
  }

.container-form{
    background-color: #dde6ee;
    height: 450px;
    border-radius: 0.5rem; 
    font-size: 20px;
    margin: auto;
    margin-top: 10px;
    
  }

  form{
    padding: 2px;
    width: 80%;
    height: 350px;
    border-radius: 0.5rem;
    margin: auto;
    text-align: center;
    background-color: #205d96;

}
.ctn-title{
    margin-top: 25px; 
    padding: 0.5px;
}
.title{
    margin-top: 25px;
    padding: 0.5px;
}
input{
  width: 90%;
  padding: 0.5rem;
  border-radius: 4px;
  box-sizing: border-box; 
  border-color: #55970a;
  margin:auto;
  margin-top:2px;
}

button {
  padding: 0.5rem 1rem;
  background-color: #e27713;
  color: black;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 20px;
  margin-right: 5px;
  font-weight: bold;  
}

textarea{
  height: 100px;
  width:90%; 
  border-radius: 4px;
  box-sizing: border-box; 
  margin-left: 2rem;
  border-color: #7de20a;
  margin:auto;
  margin-top:2px;
}


label{

display: inline-block;
color:black;
text-align: left;
margin:auto;
margin-top:2px;
margin-bottom: 2px;
color: black

}
.container-lbl{
  text-align: left;
  margin-left: 15px;
  margin-bottom: 2px;
  margin-top:10px;
}
.error-message{
  color:white;
  font-size: 16px;
}

.no-jobs-message {
  text-align: center;
  padding: 40px 20px;
  color: #666;
  font-style: italic;
  background-color: #f8f9fa;
  border-radius: 8px;
  margin: 20px;
}

.no-jobs-message p {
  margin: 0;
  font-size: 18px;
}

</style>