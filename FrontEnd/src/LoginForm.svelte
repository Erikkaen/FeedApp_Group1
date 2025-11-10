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
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ username, password }),
            });

            if (response.ok) {
                const userData = await response.json();
                dispatch("userCreated", userData);
            } else {
                const errText = await response.text();
                alert("Login failed: " + errText);
            }
        } catch (error) {
            console.error("Login error:", error);
        }
    }
</script>

<div class="component">
    <h2>Login Form</h2>
    <input placeholder="Username" bind:value={username} />
    <input type="password" placeholder="Password" bind:value={password} />
    <button class="button" on:click={login}>Login</button>
</div>