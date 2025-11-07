<script>

    import CreateUserComponent from "./components/CreateUserComponent.svelte";
    import CreatePollComponent from "./components/CreatePollComponent.svelte";
    import VoteComponent from "./components/VoteComponent.svelte";


    let currentUser = null;

    function handleUserCreated(user) {
        currentUser = user;
        console.log("Logged in as:", user.username);
    }

</script>

<main>
    <h2>FeedApp</h2>

    <!-- edit poll as well TO-DO -->

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
