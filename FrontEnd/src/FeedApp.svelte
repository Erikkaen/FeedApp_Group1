<script>
  import CreateUserComponent from "./components/CreateUserComponent.svelte";
  import CreatePollComponent from "./components/CreatePollComponent.svelte";
  import VoteComponent from "./components/VoteComponent.svelte";

  let currentUser = null;
  let pollRefresh = 0;

  function handleUserCreated(user) {
    currentUser = user;
    console.log("Logged in as:", user.username);
  }

  function handlePollCreated() {
    pollRefresh += 1;
  }
</script>

<main>
  <h2>FeedApp</h2>

  <!-- Step 1: Create user -->
  <CreateUserComponent on:userCreated={e => handleUserCreated(e.detail)} />

  <!-- Step 2: Create poll -->
  {#if currentUser}
    <CreatePollComponent on:pollCreated={handlePollCreated} />
  {/if}

  <!-- Step 3: Vote on poll -->
  {#if currentUser}
    <VoteComponent {currentUser} {pollRefresh} />
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
