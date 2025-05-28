<template>   
  <div class="container-worker">
    <div class="categories">
        <span> Carpentry</span>
        <span>Plumbing </span>
        <span>Electrical </span>
        <span>Masonry</span>
        <span>Gardening </span>
    </div>

  <div>
      <h1 class="title">Hire the best skilled workers </h1>
  </div>
    <div class="cards">
      <div v-for="worker in workers" :key="worker.id" class="card-worker">
        <img src="images/people.png"  class="profile" />
        <p><strong>ID:</strong> {{ worker.id }}</p>
        <p style="font-size: large; font-weight: bold;">{{ worker.username }}</p>
        <p style="font-weight: normal;">{{ worker.description || 'No description available' }}</p>
        
        <!-- Skills Display -->
        <div v-if="worker.skills && worker.skills.length > 0" class="skills-container">
          <p><strong>Skills:</strong></p>
          <div class="skills-list">
            <span v-for="skill in worker.skills" :key="skill.id" class="skill-tag">{{ skill.name }}</span>
          </div>
        </div>
        
        <!-- Location -->
        <p v-if="worker.location"><strong>Location:</strong> {{ worker.location }}</p>
        
        <!-- Contact Info -->
        <div class="contact-info">
          <p v-if="worker.phone">
            <img src="../../public/images/phone.png" width="25px" height="25px" style="margin-bottom: -7px;"/> {{ worker.phone }}
          </p>
          <p v-if="worker.email">
            <img src="../../public/images/email.png" width="30px" height="30px" style="margin-bottom: -10px; padding-right: 2px; margin-left: -2px;"/>{{ worker.email }}
          </p>
        </div>
        
        <!-- Hourly Rate -->
        <p v-if="worker.hourlyRate"><strong>Hourly Rate:</strong> ${{ worker.hourlyRate }}</p>
        
        <!-- Experience -->
        <p v-if="worker.experienceYears"><strong>Experience:</strong> {{ worker.experienceYears }} years</p>
        
        <!-- Rating Display -->
        <div class="rating-section">
          <div v-if="worker.averageRating === null || worker.averageRating === undefined">
            <p>No rating yet</p>
          </div>
          <div v-else-if="worker.averageRating === 5">
            <img src="images/5star.png" class="rating" />
          </div>
          <div v-else-if="worker.averageRating === 4">
              <img src="images/4star.png" class="rating" />
          </div>
          <div v-else-if="worker.averageRating === 3">
              <img src="images/3star.png" class="rating" />
          </div>
          <div v-else-if="worker.averageRating === 2">
              <img src="images/2star.png" class="rating" />
          </div>
          <div v-else-if="worker.averageRating === 1">
              <img src="images/1star.png" class="rating" />
          </div>
          <div v-else>
              <p>Rating: {{ worker.averageRating }}/5</p>
          </div>
        </div>
        
        <!-- Availability -->
        <p v-if="worker.isAvailable !== undefined">
          <strong>Status:</strong>
          <span :class="worker.isAvailable ? 'available' : 'unavailable'">
            {{ worker.isAvailable ? 'Available' : 'Busy' }}
          </span>
        </p>
        
        <button type="submit" class="button-profile" @click="sendData(worker.id)">See more</button>
      </div>
    </div>
  </div>

</template>



<script>
import FetchDataService from "../services/FetchDataService";

export default{  
    name:"DisplayWorkers",

    data() {
    return {
      workers: [],
      id:1
      
    }
    
  },

    methods:{
        fetchWorkers(){
            FetchDataService.getAllWorkers()
            .then(response =>{
                this.workers =response.data
                console.log(response)               
            })
            .catch (error =>{
                if (error.response) {
            console.log(error.response.data);
            console.log(error.response.status);
        }} );
      },

      sendData(workerId){
       
          console.log("mira lo que tengo aca", workerId)
          this.$router.push({name:"SelectedWorker", params: { workerId: workerId }})
        
      }
    },
    mounted() {
    this.fetchWorkers();
    
  },
 
}
</script>

<style>
h1{
    font-size: 50px;
    margin-left: 9rem;
    color: rgb(1, 97, 1);
    margin-top: 70px;
}


.categories {
  display: flex;
  width: 80%;
  margin-left: 10rem;
  font-size: 20px;
  font-weight: bold;
  color:#e27713;
}

.categories span {
  margin-right: 15px; 
}

.cards{
  margin: auto;
  text-align: center;
  width: 70%;
  display: grid;
  grid-gap: 10px;
  margin-top: 10rem;
  grid-template-columns: repeat(4, minmax(250px, 1fr));
  text-align: center;


}
.card-worker{
  box-sizing: border-box;
  border-radius: 0.5rem;
  display: flex;
  flex-direction: column;
  text-align: center;
  transition: transform 0.3s ease;
  max-width:98%;
  font-weight: bold;
  background-color:white;
  margin-left: 20px;
  border: 1px solid rgb(31, 142, 175);
  line-height: 1; 
}
.profile{
  width: 100px;
  height: 100px;
  margin: auto;
  margin-top: 15px;
}

.rating{
  max-width: 110px;
  height: 25px;
  margin: auto;
  margin-top: 15px;
  margin-bottom: 15px;
  display:inline
}
.rating-container img,
.rating-container span {
    display: inline-block;
    margin-right: 5px;
    
}
.subtitle-worker{
  text-align: left;
  margin-bottom: 20px;
}

.button-profile{
  display: inline-block;
  padding: 0.5rem 1rem;
  background-color: #e27713;
  color: rgb(32, 20, 20);
  width: 120px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
 
  margin: auto;
  margin-bottom: 10px;
}

.skills-container {
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

.contact-info {
  text-align: left;
  margin: 10px 0;
}

.contact-info p {
  margin: 5px 0;
}

.rating-section {
  margin: 10px 0;
}

.available {
  color: green;
  font-weight: bold;
}

.unavailable {
  color: red;
  font-weight: bold;
}

</style>