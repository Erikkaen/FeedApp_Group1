<script>
    import CreatePollComponent from "./components/CreatePollComponent.svelte";
    import VoteComponent from "./components/VoteComponent.svelte";

    export let currentUser;
    let pollRefresh;
</script>

<main>
    <h2>Welcome, {currentUser.username}!</h2>
    <CreatePollComponent {currentUser} on:pollCreated={() => (pollRefresh = Date.now())} />
    <VoteComponent {currentUser} {pollRefresh} />
</main>

