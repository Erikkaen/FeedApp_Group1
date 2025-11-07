<script>
  import CreateUserComponent from "./components/CreateUserComponent.svelte";
  import CreatePollComponent from "./components/CreatePollComponent.svelte";
  import VoteComponent from "./components/VoteComponent.svelte";
  import LogIn from "./LogInPage.svelte";
  import RegistratedUserPage from "./RegistratedUserPage.svelte";
  import AnonymousUserPage from "./AnonymousUserPage.svelte";


  let currentUser = null;
  let pollRefresh = 0;
  let page = "LogIn"; // <-- current page

  function handleUserCreated(user) {
    currentUser = user;
    console.log("Logged in as:", user.username);
    page = 'regiUser'
  }

  function handlePollCreated() {
    pollRefresh += 1;
  }


</script>


<main>
    {#if page === 'LogIn'}
        <LogIn/>

    {:else if page === 'regiUser'}
        <RegistratedUserPage/>

    {:else if page === 'AnonyUser'}
        <AnonymousUserPage/>
    {/if}

</main>

<style>
  main {
    max-width: 800px;
    margin: auto;
    padding: 20px;
    font-family: sans-serif;
  }
</style>
