<template>
  <div class = "container-singup">
      <img src="images/workers2.jpg" class="img-workers" />

      <div class="container-form">
          <div class ="container-grettings">
          <h2 class ="grettings">Wellcome to Handyman</h2>
          <img src="images/waving-hand.png" class="img-hands" />
      </div>
     <div class= "container-type-user ">
          <button @click="userType = 'worker'" class="type-user ">I am worker</button>
          <button @click="userType = 'empresa'" class="type-user2">I'm hiring </button>
      </div>

      <form v-if="userType === 'worker'" @submit.prevent="submitWorkerForm">
        <div class="names">
        <label for="firstName">First Name:</label>
        <input type="text" id="firstName" v-model="worker.firstname" placeholder="Enter your first name"  required/>
        <label for="lastName">Last Name:</label>
        <input type="text" id="lastName" v-model="worker.lastname" placeholder="Enter your last name"  required/>
      </div>
      <div>
        <label for="email">Email:</label>
        <input type="text" id="email" v-model="worker.email" placeholder="Enter your email"  required/>
      </div>
      <div>
        <label for="password">Password:</label>
        <input type="password" id="password" v-model="worker.password" placeholder="Enter your password" pattern=".{6,}" required/>
      </div>
      <div>
        <label for="location">Location:</label>
        <input type="text" id="location" v-model="worker.location" placeholder="Enter your location"  required />
      </div>
      <div>
        <label for="description">Description:</label>
        <textarea v-model="worker.description" placeholder="Describe your profile"  required></textarea>
      </div>
        <div class="form-group">
          <label for="skill">Skill:</label>
          <select id="skill" v-model="tempSkill">
            <option value="" disabled>Select skill</option>
            <option value="Carpentry">Carpentry</option>
            <option value="Plumbing">Plumbing</option>
            <option value="Electrical">Electrical</option>
            <option value="Masonry">Masonry</option>
            <option value="Gardening">Gardening</option>
          </select>
        </div>
      <div>
        <button type="submit">Sign up</button>
      </div>

    </form>

      <form v-else-if="userType === 'empresa'" @submit.prevent="submitUserForm">
      <div>
        <label for="firtsname">First Name:</label>
        <input type="text" id="firstname"  v-model="user.firstname" placeholder="Enter your first name"  required/>
      </div>
      <div>
        <label for="lastname">Last Name:</label>
        <input type="text" id="lastname" v-model="user.lastname" placeholder="Enter your last name"  required/>
      </div>
      <div>
        <label for="email">Email:</label>
        <input type="email" id="email" v-model="user.email" placeholder="Enter your email"  required/>
      </div>
      <div>
        <label for="password">Password:</label>
        <input type="password" id="password" v-model="user.password" placeholder="Enter your password" pattern=".{6,}" required/>
      </div>
      <div>
        <label for="credit">Credit:</label>
        <input type="text" id="credit" v-model="user.credit" placeholder="Enter your credit"  required/>
      </div>
      <div>
          <button type="submit">Sign up </button>
      </div>
    </form>

    <!-- Display success/error messages -->
    <div v-if="successMessage" class="success-message">
      {{ successMessage }}
    </div>
    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>

  </div>

  </div>
</template>

<script>
import FetchDataService from "../services/FetchDataService";


// Define the capitalizeFirstLetter function outside of the component
function capitalizeFirstLetter(string) {
return string.charAt(0).toUpperCase() + string.slice(1);
}

export default {
name: "SignUp",
data() {
  return {
    userType: null,
    tempSkill : "",
    worker: {
      username: "",
      firstname: "",
      lastname: "",
      email: "",
      password: "",
      location: "",
      description: "",
      skills: []
    },
    user: {
      username: "",
      firstname: "",
      lastname: "",
      password: "",
      email: "",
      credit: 1000.0
    },
    successMessage: null,
    errorMessage: null
  };
},
methods: {
  addSkill() {
    if (this.tempSkill && !this.worker.skills.includes(this.tempSkill)) {
      this.worker.skills.push(this.tempSkill);
    }
    this.tempSkill = "";
  },

  submitWorkerForm() {
    // Combine firstName and lastName to create the username
    this.worker.username = capitalizeFirstLetter(this.worker.firstname) + "." + capitalizeFirstLetter(this.worker.lastname);

    this.addSkill();
    
    // Prepare worker data for backend
    const workerData = {
      ...this.worker,
      skillNames: this.worker.skills
    };

    console.log("Creating worker:", workerData);

    FetchDataService.createWorker(workerData)
      .then(response => {
        console.log("Worker created successfully:", response.data);
        this.successMessage = "Worker created successfully! You can now login.";
        this.errorMessage = null;
        
        // Show success message briefly then redirect
        setTimeout(() => {
          this.$router.replace('/login');
        }, 2000);
      })
      .catch(error => {
        console.error("Error creating worker:", error);
        if (error.response && error.response.data) {
          this.errorMessage = error.response.data.message || "Error creating worker. Please try again.";
        } else {
          this.errorMessage = "Error creating worker. Please try again.";
        }
        this.successMessage = null;
      });
  },
  
  submitUserForm() {
    // Combine firstName and lastName to create the username
    this.user.username = capitalizeFirstLetter(this.user.firstname) + "." + capitalizeFirstLetter(this.user.lastname);

    console.log("Creating user:", this.user);

    FetchDataService.createUser(this.user)
      .then(response => {
        console.log("User created successfully:", response.data);
        this.successMessage = "User created successfully! You can now login.";
        this.errorMessage = null;
        
        // Show success message briefly then redirect
        setTimeout(() => {
          this.$router.replace('/login');
        }, 2000);
      })
      .catch(error => {
        console.error("Error creating user:", error);
        if (error.response && error.response.data) {
          this.errorMessage = error.response.data.message || "Error creating user. Please try again.";
        } else {
          this.errorMessage = "Error creating user. Please try again.";
        }
        this.successMessage = null;
      });
  }
}
};

</script>



<style scoped>
.container-singup {
  display: flex;
  align-items: center;
  justify-content: center;
}
.img-workers{
  float:left;
  margin-left: 5rem;
  width: 50%;
  margin-bottom: 20%;
  height: 600px;

}

form{

width: 80%;
height: 650px;
border-radius: 0.5rem;
margin:auto;
text-align: left;

}


label{
margin-left: 2rem;
display: block;
color:black;
margin-top: 8px;
text-align: left;

}

input, textarea, select{
margin-top: 5px;
width: 80%;
padding: 0.8rem;
border-radius: 0.5rem;
box-sizing: border-box; 
margin-left: 2rem;
border-color: #7de20a;
}

select{
  padding: 0.5rem;
}

button {
padding: 0.8rem ;
background-color: #e27713;
color: white;
border: none;
border-radius: 4px;
cursor: pointer;
margin-top: 20px;
margin-left: 2rem;
font-weight: bold;
width: 80%;


}
.container-form{
 /* background-color: #e27713;*/
  width: 40%;
  text-align: center;
  /*background-color: #df8d1b;*/
  height: 1000px;
}
.container-grettings{
 /*background-color: #7de20a;*/
  margin-top: 10px;
  display: inline-block;
  margin-right: 5px;
}
.grettings{
  display: inline-block;
}
.img-hands{
 width: 60px;
height: 60px;
margin: auto;
margin-top: 15px;
display:inline
}
.message{
  margin: auto;
  margin-left: 7rem;
  color:#5b7404
}
.nav-link{
  font-size: 20px;

  padding: 0.5rem;
  margin-top: 15px;
  color:#5b7404;
  font-weight: bold;
  text-align: center;
  margin-left: 5rem;
}
textarea{
  height: 100px;
}
.names.label{
  display: inline;
}
.container-type-user{
  text-align: center;
  margin-right: 40px;
}
.type-user, .type-user2{
  width: 180px;
  height: 100px;
  background-color: blue;
  display: inline-block;
  margin-bottom: 10px;
  margin-right: 10px;
 
}
.type-user2{
  margin-bottom: 0px;
  margin-top: 10px;
}

.success-message {
  color: green;
  font-weight: bold;
  text-align: center;
  margin: 20px;
  padding: 10px;
  background-color: #d4edda;
  border: 1px solid #c3e6cb;
  border-radius: 4px;
}

.error-message {
  color: red;
  font-weight: bold;
  text-align: center;
  margin: 20px;
  padding: 10px;
  background-color: #f8d7da;
  border: 1px solid #f5c6cb;
  border-radius: 4px;
}
</style>