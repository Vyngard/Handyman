<template>

    <div class="container-cards">
        
        <div class="cards-description">
           
            <div class ="personal-data">
                <img src="images/people.png" class="profile" />
                <p class="data-description"><strong>Name:</strong> {{ worker.username }}</p>
                <p class="data-description"><strong>ID:</strong> {{ worker.id }}</p>
                <p class="data-description">{{ worker.description || 'No description available' }}</p>
                <div v-if="worker.skills && worker.skills.length > 0" class="skills-section">
                    <p><strong>Skills:</strong></p>
                    <div class="skills-list">
                        <span v-for="skill in worker.skills" :key="skill.id" class="skill-tag">{{ skill.name }}</span>
                    </div>
                </div>
                <p v-if="worker.averageRating !== undefined"><strong>Rating:</strong> {{ worker.averageRating }}/5</p>
                <p v-if="worker.hourlyRate"><strong>Hourly Rate:</strong> ${{ worker.hourlyRate }}</p>
                <p v-if="worker.experienceYears"><strong>Experience:</strong> {{ worker.experienceYears }} years</p>
            </div>


            <div class="setting">
                <div class = "edit-preferences">
                    <h2>Preferences</h2>
                <img src="images/editar.png" class="edit" @click="editPreferences" /> 
                </div>
                <div  v-if="showForm" class=" container form">
                    <form >
                        
                        <input type="text" id="location" v-model="worker_settings.location" placeholder="Location">
                       
                        <input type="text" id="phone" v-model="worker_settings.phone" placeholder="Phone">
                        
                        <input type="text" id="email" v-model="worker_settings.email" placeholder="Email">
                        
                        <button type="submit" @click="savePreferences">Save</button>
                        <button type="submit" @click="hideForm">Cancel</button>
                    </form>
                </div>
                <div v-else>
                    <p><strong>Location:</strong> {{ worker.location }}</p>
                    <p><strong>Availability:</strong> {{ worker.workingHours }}</p>
                    <p><strong>Preferred communication:</strong> {{ worker.preferredCommunication }}</p>
                    <p><strong>Phone:</strong> {{ worker.phone }}</p>
                    <p><strong>Email:</strong> {{ worker.email }}</p>
                </div>
           
            </div>
        </div>

        <div class="card-jobs">
            <div class=" input-search">
                <input  tye="text" placeholder="Search..."/>
            </div>
            <div class="title-jobs">
                <button @click="fetchJobs" class="fetchData">Best Matches</button>
                <button @click="fetchRecentJobs" class="fetchData">Recent Jobs</button>
            </div>          
                
            <div v-if="jobs.length" >
                    <div v-for="(job, index) in jobs" :key="index" class="job">
                        <div v-if="jobs.length && displayNewJobs">
                            <p class="desc-jobs1">Posted: {{ formatDate(job.createdAt) || date }}</p>
                            <p class="desc-job"><strong>Title:</strong> {{ job.title }}</p>
                            <p class="desc-job"><strong>Description:</strong> {{ job.description }}</p>
                            <p class="desc-job" v-if="job.client"><strong>Client:</strong> {{ job.client.username }}</p>
                            <p class="desc-job"><strong>Budget:</strong> ${{ job.budget }}</p>
                            <p class="desc-job" v-if="job.location"><strong>Location:</strong> {{ job.location }}</p>
                            <p class="desc-job" v-if="job.skills && job.skills.length > 0">
                                <strong>Skills Required:</strong> {{ job.skills.map(skill => skill.name).join(', ') }}
                            </p>
                            <p class="desc-job"><strong>Job ID:</strong> {{ job.id }}</p>
                            <div class="container-apply-btn">
                                <button class="apply-btn" @click="applyJob(job.id)">Apply Now</button>
                            </div>
                        </div>
                        <div v-else-if="jobs.length && displayOldJobs">
                            <p class="desc-job"><strong>Title:</strong> {{ job.title }}</p>
                            <p class="desc-job"><strong>Description:</strong> {{ job.description }}</p>
                            <p class="desc-job" v-if="job.client"><strong>Client:</strong> {{ job.client.username }}</p>
                            <p class="desc-job"><strong>Budget:</strong> ${{ job.budget }}</p>
                            <p class="desc-job" v-if="job.location"><strong>Location:</strong> {{ job.location }}</p>
                            <p class="desc-job"><strong>Job ID:</strong> {{ job.id }}</p>
                            <p class="desc-job"><strong>Status:</strong>
                                <span :class="job.isCompleted ? 'completed' : 'in-progress'">
                                    {{ job.isCompleted ? 'Completed' : 'In progress' }}
                                </span>
                            </p>
                            <p class="desc-job" v-if="job.createdAt"><strong>Created:</strong> {{ formatDate(job.createdAt) }}</p>
                            <p class="desc-job" v-if="job.completedAt"><strong>Completed:</strong> {{ formatDate(job.completedAt) }}</p>
                            <p class="desc-job"><strong>Rating:</strong>
                                <span v-if="job.rating !== null && job.rating !== undefined">{{ job.rating }}/5</span>
                                <span v-else>{{ rating }}</span>
                            </p>
                        </div>
                    </div>
               
            </div>
        </div>
    </div>

</template>

<script>

import FetchDataServices from  '../services/FetchDataService'

export default{
    name: "WorkerDetails",

    data(){
        return{
            newLogin: false,
            worker: {
                username:"",
                description:"",
                skills: []
            },
            jobs:[],
            date: "",
            random_number: 0, 
            displayNewJobs: true,
            displayOldJobs: false,
            showForm: false ,
            worker_settings:{
                location:"",
                availability: "",
                communication:"",
                phone: "",
                email: ""
            },
            applied: false,
            errorMessage:"",
            errorMessage2:"", 
            rating: "Rating no available", 
            status: "In progess",
            message: "We are seeking a professional  for ",
            message2: " . It's required knowledge of construction materials and carpentry techniques.Ability to interpret blueprints and follow instructions.Teamwork skills and ability to meet deadlines"


        }

    },

    methods:{

        retrieveWorker(){
            this.displayNewJobs = true;
            this.displayOldJobs = false;

            const id = localStorage.getItem('workerId')
            console.log("SID:" + id)
            FetchDataServices.getWorkerById(id)
            .then(response =>{
                this.worker = response.data
                console.log("username")
                console.log(this.worker.username)
                this.fetchJobs()
               
            })
            .catch(error=>{
                console.log(error)
            })
            
        },

        fetchJobs() {
            if (!this.worker.skills || this.worker.skills.length === 0) {
                console.log('No skills found for worker');
                return;
            }

            // Get jobs for the first skill (or you could combine all skills)
            const firstSkill = this.worker.skills[0];
            const todayDate = new Date();
            this.random_number = Math.floor(Math.random() * 3) + 1;
            todayDate.setDate(todayDate.getDate() - this.random_number);
            const newDate = todayDate.toLocaleDateString();

            console.log('Fetching jobs for skill:', firstSkill.name);
            
            FetchDataServices.getjobBySkill(firstSkill.name)
                .then(response => {
                    console.log('Jobs response:', response.data);
                    this.jobs = response.data.filter(job => job.worker === null);
                    this.date = newDate;
                    this.displayOldJobs = false;
                    this.displayNewJobs = true;
                    console.log('Filtered available jobs:', this.jobs);
                })
                .catch(error => {
                    console.error('Error fetching jobs by skill:', error);
                    // Fallback: fetch all jobs and filter client-side
                    FetchDataServices.getAllJobs()
                        .then(response => {
                            this.jobs = response.data.filter(job =>
                                job.worker === null &&
                                job.skills &&
                                job.skills.some(jobSkill =>
                                    this.worker.skills.some(workerSkill =>
                                        workerSkill.name === jobSkill.name
                                    )
                                )
                            );
                            this.date = newDate;
                            this.displayOldJobs = false;
                            this.displayNewJobs = true;
                        })
                        .catch(fallbackError => {
                            console.error('Error fetching all jobs:', fallbackError);
                        });
                });
        },
        fetchRecentJobs(){
            const id = localStorage.getItem('workerId')
            console.log('Fetching recent jobs for worker:', id);
            
            FetchDataServices.getjobByWorkerId(id)
            .then(response=>{
                console.log('Worker jobs response:', response.data);
                this.jobs = response.data;
                this.displayNewJobs = false;
                this.displayOldJobs = true;
            })
            .catch(error => {
                console.error('Error fetching worker jobs:', error);
                // Fallback: try the alternative endpoint
                FetchDataServices.getWorkerJobs(id)
                .then(response => {
                    console.log('Alternative worker jobs response:', response.data);
                    this.jobs = Array.from(response.data) || [];
                    this.displayNewJobs = false;
                    this.displayOldJobs = true;
                })
                .catch(fallbackError => {
                    console.error('Error with fallback endpoint:', fallbackError);
                });
            });
        },
        checklogin() {
        localStorage.setItem('newLogin', false);
        this.newLogin = localStorage.getItem('newLogin');
        console.log(this.newLogin)
        window.location.reload();
      },

      editPreferences(){
        this.showForm=true;
      },
      hideform(){
         this.showForm=false;
      },
      savePreferences(event){
        event.preventDefault()
        const id = localStorage.getItem('workerId')
        
        if(this.worker_settings.location.trim() === '' ){
            this.worker_settings.location = this.worker.location
        }
        if(this.worker_settings.phone.trim() === '' ){
            this.worker_settings.phone = this.worker.phone
        }
        if(this.worker_settings.email.trim() === '' ){
            this.worker_settings.email = this.worker.email
        }       
    
        let updateData = {
            username:this.worker.username,
            description:this.worker.description,
            location:this. worker_settings.location,
            phone: this.worker_settings.phone,
            email: this.worker_settings.email
        };
        console.log(updateData)
        FetchDataServices.updateWorker(id,updateData)
        .then(response=>{
            console.log(response.data)
            this.showForm=false;
            this.worker.location = updateData.location;
            this.worker.phone = updateData.phone;
            this.worker.email = updateData.email;
            this.worker_settings.location="";
            this.worker_settings.phone="";
            this.worker_settings.email="";
        })        
        .catch(error => {
                    console.error(error);
        })
      },
      applyJob(idJob){        
        let updatedJob={
            workerId: this.worker.id
        }
        FetchDataServices.updateJobs(idJob, updatedJob)
        .then(response=>{
            this.fetchJobs(); 
            this.fetchRecentJobs(); 
            console.log(response) 
        })
        .catch(error => {
                    console.error(error);
       })
     },

     formatDate(dateString) {
         if (!dateString) return 'N/A';
         return new Date(dateString).toLocaleDateString();
     }
   },
    mounted(){
      this.retrieveWorker()          
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
    width: 90%;
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
  height: 750px;
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
    /*background-color: brown;*/
    background-color: rgb(230, 239, 247);
    border-radius: 0.5rem;
    padding: 5px;
    font-size: 18px;
    margin-top: 15px;
    color: black;
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
padding: 3px;
width: 80%;
height: 350px;
border-radius: 0.5rem;
margin: auto;
text-align: center;


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
label{

display: inline-block;
color:black;
text-align: left;
margin:auto;
margin-top:2px;
margin-bottom: 2px;
color: black

}

.skills-section {
    margin: 10px 0;
    text-align: left;
}

.skills-list {
    display: flex;
    flex-wrap: wrap;
    gap: 5px;
    margin-top: 5px;
}

.skill-tag {
    background-color: #e27713;
    color: white;
    padding: 3px 8px;
    border-radius: 12px;
    font-size: 12px;
    font-weight: bold;
}

.completed {
    color: green;
    font-weight: bold;
}

.in-progress {
    color: orange;
    font-weight: bold;
}

</style>