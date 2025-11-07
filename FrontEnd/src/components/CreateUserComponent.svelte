<script>
  import { createEventDispatcher } from "svelte";
  const dispatch = createEventDispatcher();

  let username = "";
  let email = "";


  async function guest() {

  }

  async function createUser() {
    const userData = { username, email };

    await fetch("http://localhost:8080/users", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(userData),
    });

    dispatch("userCreated", userData);
  }
</script>

<div class="component">
  <!-- <h2>Continue as guest</h2> -->
  <button on:click={guest}>Continue as guest</button>
</div>

<div class="component">
  <h2>Create User</h2>
  <input placeholder="Username" bind:value={username} />
  <input placeholder="Email" bind:value={email} />
  <button on:click={createUser}>Create User</button>
</div>


