<script>
    import VoteComponent from "./components/VoteComponent.svelte";
    import {createEventDispatcher} from "svelte";
    const dispatch = createEventDispatcher();

    let currentUser = { username: "guest" };
    let pollRefresh;

    function home() {
        dispatch("homePage");
    }
</script>

<main>
    <button class="backButton" on:click={home}>Go back</button>

    <h2>Welcome, Guest</h2>

    <VoteComponent {currentUser} {pollRefresh} />
</main>
