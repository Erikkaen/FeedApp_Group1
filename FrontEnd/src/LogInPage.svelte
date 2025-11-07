<script>
    import CreateUserComponent from "./components/CreateUserComponent.svelte";


    let currentUser = null;

    function handleUserCreated(user) {
        currentUser = user;
        console.log("Logged in as:", user.username);
    }

</script>

<main>
    <h2>LogIn</h2>

    <!-- Step 1: Create user -->
    <CreateUserComponent on:userCreated={e => handleUserCreated(e.detail)} />


</main>

<style>
    main {
        max-width: 800px;
        margin: auto;
        padding: 20px;
        font-family: sans-serif;
    }
</style>
