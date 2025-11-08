<script>
    import { createEventDispatcher } from "svelte";
    const dispatch = createEventDispatcher();

    let username = "";
    let email = "";

    async function login() {
        try {
            const response = await fetch("http://localhost:8080/users", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ username, email }),
            });

            if (response.ok) {
                const userData = await response.json();
                dispatch("userCreated", userData);
            } else {
                console.error("Login failed");
            }
        } catch (error) {
            console.error("Login error:", error);
        }
    }
</script>

<div class="component">
    <h2>Login Form</h2>
    <input placeholder="Username" bind:value={username} />
    <input type="email" placeholder="email" bind:value={email} />
    <button class="button" on:click={login}>Login</button>
</div>