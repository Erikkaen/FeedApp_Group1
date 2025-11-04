<script>
  import { createEventDispatcher } from "svelte";
  const dispatch = createEventDispatcher();

  let username = "";
  let email = "";

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
  <h2>Create User</h2>
  <input placeholder="Username" bind:value={username} />
  <input placeholder="Email" bind:value={email} />
  <button on:click={createUser}>Create User</button>
</div>

<style>
  .component {
    border: 1px solid #ccc;
    padding: 20px;
    margin-bottom: 30px;
    border-radius: 5px;
    width: 250px;
  }
  input {
    display: block;
    margin-bottom: 10px;
    padding: 5px;
    width: 100%;
  }
  button {
    background: lightgrey;
    border: none;
    padding: 6px 12px;
    border-radius: 5px;
    cursor: pointer;
  }
  button:hover {
    background: #ddd;
  }
</style>
