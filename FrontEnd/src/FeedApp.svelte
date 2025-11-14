<script>
  import CreateUserComponent from "./components/CreateUserComponent.svelte";
  /* import CreatePollComponent from "./components/CreatePollComponent.svelte";
  import VoteComponent from "./components/VoteComponent.svelte"; */
  import HomePage from "./HomePage.svelte";
  import LoggedIn from "./LoggedIn.svelte";
  import GuestPage from "./GuestPage.svelte";
  import LoginForm from "./LoginForm.svelte";


  let currentUser = null;
  // let pollRefresh = 0;
  let currentPage = "home";

  function handleUserCreated(user) {
    currentUser = user;
    console.log("Registrated user: ", user.username);
    currentPage = "loggedIn"
  }

  function continueAsGuest() {
    currentPage = "anonymous";
  }

  function goToLogin() {
    currentPage = "loginForm";
  }

  function goToRegister() {
    currentPage = "registerForm";
  }
  function goToHome() {
    currentPage = "home";
  }

  /* function handlePollCreated() {
    pollRefresh += 1;
  } */

</script>


<main>
  {#if currentPage === "home"}
    <HomePage
      on:userCreated={(e) => handleUserCreated(e.detail)}
      on:guest={continueAsGuest}
      on:login={goToLogin}
      on:register={goToRegister}
    />

  {:else if currentPage === "loggedIn"}
    <LoggedIn {currentUser}
      on:homePage={goToHome}/>

  {:else if currentPage === "loginForm"}
    <LoginForm 
      on:userCreated={(e) => handleUserCreated(e.detail)}
      on:homePage={goToHome}
    />
  {:else if currentPage === "registerForm"}
    <CreateUserComponent
      on:userCreated={(e) => handleUserCreated(e.detail)}
      on:homePage={goToHome}
    />
  {:else if currentPage === "anonymous"}
    <GuestPage
      on:homePage={goToHome}
      on:register={goToRegister}
    />
  {/if}

</main>