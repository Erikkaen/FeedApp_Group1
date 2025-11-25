<script>
    import { createEventDispatcher } from "svelte";
    const dispatch = createEventDispatcher();

    let username = "";
    let email = "";
    let password = "";

    async function login() {
        try {
            const response = await fetch("http://localhost:8080/users/login", {
                method: "POST",
                headers: { "Content-Type": "application/json"  },
                body: JSON.stringify({ username, password }),
                credentials: 'include',
            });

            if (response.ok) {
                dispatch("userCreated", {username});

            } else {
                alert("Login failed");
            }
        } catch (error) {
            console.error("Login error:", error);
        }
    }

    function home() {
        dispatch("homePage");
    }
</script>

<button class="backButton" on:click={home}>Go back</button>

<div class="component">
    <h2>Login Form</h2>
    <input placeholder="Username" bind:value={username} />
    <input type="password" placeholder="Password" bind:value={password} />
    <button class="button" on:click={login}>Login</button>
</div>
