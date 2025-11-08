<script>
    import CreateUserComponent from "./components/CreateUserComponent.svelte";
    import { createEventDispatcher } from "svelte";
    const dispatch = createEventDispatcher();

    function register() {
        dispatch("register");
    }

    function login() {
        dispatch("login");
    }

    function guest() {
        dispatch("guest");
    }

</script>

<main>
    <h1>FeedApp</h1>
    <h2>Create polls or vote on polls!</h2>

    <!-- Go to registration -->
    <div class="homeComponent">
        <h3>Don't have an account?</h3>
        <button class="homeButton" on:click={register}>Register account</button>
    </div>

    <!-- Login -->
    <div class="homeComponent">
    <h3>Login</h3>
    <button class="homeButton" on:click={login}>Login</button>
    </div>
   
    <!-- continue as guest -->
    <button class="homeButton" on:click={guest}>Continue as Guest</button>
</main>
