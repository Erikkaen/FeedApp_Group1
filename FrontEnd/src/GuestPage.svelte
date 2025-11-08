<script>
    import VoteComponent from "./components/VoteComponent.svelte";

    let currentUser = { username: "guest" };
    let pollRefresh;
</script>

<main>
    <h2>Welcome, Guest</h2>
    <VoteComponent {currentUser} {pollRefresh} />
</main>
