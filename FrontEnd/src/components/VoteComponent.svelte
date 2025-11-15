<script>
  export let currentUser;
  export let pollRefresh;
  import { onMount } from "svelte";

  let polls = [];
  let selectedOptions = {};
  let voting = {};
  let guestId = localStorage.getItem("guestId")

  async function fetchPolls() {
      try {
          const res = await fetch("http://localhost:8080/polls");
          if (!res.ok) throw new Error("Failed to fetch polls");

          const data = await res.json();
          // Handle both array and object responses safely
          polls = Array.isArray(data) ? data : Object.values(data);

          console.log("Fetched polls:", polls);
      } catch (err) {
          console.error("Error fetching polls:", err);
      }
  }



  onMount(fetchPolls);

  $: if (pollRefresh !== undefined) {
    fetchPolls();
  }

  if (!guestId) {
      guestId = crypto.randomUUID();
      localStorage.setItem("guestId", guestId);
  }

  async function submitVote(pollId) {
      const optionId = selectedOptions[pollId];
      if (!optionId) {
          alert("Please select an option before voting!");
          return;
      }

      const identifier = currentUser?.username || guestId;
      const voteData = { optionId, publishedAt: new Date().toISOString() };

      const res = await fetch(`http://localhost:8080/votes/${pollId}/${identifier}`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(voteData),
      });

      if (res.ok) {
          alert("Vote recorded successfully!");
          fetchPolls(); // refresh to see updated count
      } else if (res.status === 409) {
          alert("You have already voted in this poll!");
      } else {
          alert("Something went wrong submitting your vote.");
      }
  }



</script>

<div class="component">
  <h2>Vote on Polls</h2>

    {#if polls.length === 0}
        <p>No polls available yet.</p>
    {:else}
        {#each polls as poll (poll.id)}
            <div class="poll">
                <h3>{poll.question}</h3>
                <form on:submit|preventDefault={() => submitVote(poll.id)}>
                    <ul>
                        {#each poll.options as opt}
                            <li>
                                <label class="poll-option">
                                    <input
                                            type="radio"
                                            name={`option-${poll.id}`}
                                            bind:group={selectedOptions[poll.id]}
                                            value={opt.id}
                                            disabled={voting[poll.id]}
                                    />
                                    {opt.presentationOrder}. {opt.caption}
                                    {#if opt.voteCount !== undefined}
                                        <span class="votes">Votes: {opt.voteCount}</span>
                                    {/if}
                                </label>
                            </li>
                        {/each}
                    </ul>
                    <button type="submit" disabled={voting[poll.id] || !selectedOptions[poll.id]}>
                        Vote
                    </button>
                </form>
            </div>
        {/each}
    {/if}

</div>


