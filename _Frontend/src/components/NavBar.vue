<template>
  <div class="nav-container">
    <nav class="navbar">
      <div class="container">
        <router-link to="/" class="brand">
          <span class="text-2xl font-semibold">HandyMan</span>
        </router-link>

        <ul class="nav-links">
          <li>
            <router-link to="/" class="nav-link">Home</router-link>
          </li>
          <li>
            <router-link to="/workers" class="nav-link">Workers</router-link>
          </li>
          <li>
            <router-link to="/jobs" class="nav-link">Jobs</router-link>
          </li>
          <li v-if="isLoggedIn && userType==='user' ">
            <router-link to="/userdetails" class="nav-link">Profile</router-link>
          </li>
          <li v-if="isLoggedIn && userType==='worker'">
            <router-link to="/workerdetails" class="nav-link">Profile</router-link>
          </li>
          <li v-if="isLoggedIn">
            <router-link to="/messages" class="nav-link">Messages</router-link>
          </li>
          <li v-if="!isLoggedIn">
            <router-link to="/login" class="nav-link">Login</router-link>
          </li>
          <li v-if="!isLoggedIn">
            <router-link to="/signup" class="nav-link">Sign up</router-link>
          </li>
          <li v-if="isLoggedIn">
            <router-link to="/" class="nav-link"><a href="#" @click="logout">Logout</a></router-link>
          </li>
        </ul>
      </div>
    </nav>
  </div>
</template>

<script>
export default {
  name: "navBar",

  data() {
    return {
      isLoggedIn: false,
      userType: null,
    };
  },
  
  methods: {
    logout() {
      localStorage.removeItem('token');
      localStorage.removeItem('userId');
      localStorage.removeItem('workerId');
      localStorage.removeItem('userType');
      localStorage.removeItem('fullName');
      this.isLoggedIn = false;
      this.userType = null;
      this.$router.push('/');
    },
    
    checkUserType(){
      this.userType = localStorage.getItem('userType');
      console.log("user type: " + this.userType)
    },
    
    updateLoginStatus() {
      const token = localStorage.getItem('token');
      this.isLoggedIn = !!token;
      if (this.isLoggedIn) {
        this.checkUserType();
      } else {
        this.userType = null;
      }
    }
  },

  mounted() {
    this.updateLoginStatus();
    
    // Listen for storage changes (when login happens in another tab)
    window.addEventListener('storage', this.updateLoginStatus);
    
    // Check for login status changes periodically
    this.loginCheckInterval = setInterval(() => {
      this.updateLoginStatus();
    }, 1000);
  },
  
  beforeUnmount() {
    window.removeEventListener('storage', this.updateLoginStatus);
    if (this.loginCheckInterval) {
      clearInterval(this.loginCheckInterval);
    }
  },

  // watch: {
  //   if (newLogin) {
  //     console.log("ddddddddddddddddddddddddddd")

  //     localStorage.setItem('newLogin', false);
  //     console.log(newLogin)
  //     window.location.reload();
  //   }
  // }
};
</script>


<style>
.navbar {

  border-radius: 0.5rem;
  padding: 1rem;
  margin: auto;
  width: 90%;
}

.container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.brand {
  text-decoration: none;
  color: #079129;
  font-size: 35px;

}

.nav-links {
  list-style: none;
  display: flex;
  align-items: center;
}

.nav-link {
  text-decoration: none;
  color: #0a0a0a;
  margin-left: 1rem;
  font-size: 25px;
  margin-right: 2rem;
  
  
}

.nav-link:hover {
  color: #ec991c;
}


</style>