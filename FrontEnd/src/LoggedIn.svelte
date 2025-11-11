<script>
    import CreatePollComponent from "./components/CreatePollComponent.svelte";
    import VoteComponent from "./components/VoteComponent.svelte";
    import {createEventDispatcher} from "svelte";
    const dispatch = createEventDispatcher();


    export let currentUser;
    let pollRefresh;

    function home() {
        dispatch("homePage");
    }
</script>

<button class="backButton" on:click={home}>Go back</button>

<main>
    <h2>Welcome, {currentUser.username}!</h2>
    <CreatePollComponent {currentUser} on:pollCreated={() => (pollRefresh = Date.now())} />
    <VoteComponent {currentUser} {pollRefresh} />
</main>

