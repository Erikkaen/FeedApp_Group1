<script>
  import { createEventDispatcher } from "svelte";
  const dispatch = createEventDispatcher();

  let username = "";
  let email = "";
  let password = "";



  async function createUser() {
      const userData = { username, email, password };

      try {
          const res = await fetch("http://localhost:8080/users", {
              method: "POST",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify(userData),
          });

          if (!res.ok) {
              console.error("Registration failed");
              return;
          }

          const createdUser = await res.json();
          dispatch("userCreated", createdUser);
      } catch (err) {
          console.error("Error registering:", err);
      }
  }
  function home() {
      dispatch("homePage");
  }
</script>

<div class="component">
    <h2>Register</h2>
    <input placeholder="Username" bind:value={username} />
    <input type="email" placeholder="Email" bind:value={email} />
    <input type="password" placeholder="Password" bind:value={password} />
    <button on:click={createUser}>Register</button>
</div>
<button class="backButton" on:click={home}>Go back</button>
