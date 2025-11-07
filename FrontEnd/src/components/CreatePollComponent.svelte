<script>
  let question = "";
  let options = [""];

  import { createEventDispatcher } from "svelte";
  const dispatch = createEventDispatcher();

  async function createPoll() {
    const pollData = {
      question,
      publishedAt: new Date().toISOString(),
      options: options.map((opt, i) => ({
        caption: opt,
        presentationOrder: i + 1
      }))
    };

    const res = await fetch("http://localhost:8080/polls", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(pollData),
    });

    const createdPoll = await res.json();
    console.log("Created poll:", createdPoll);
    dispatch("pollCreated", { poll: createdPoll });
  }

  function addOption() {
    options = [...options, ""];
  }
</script>

<div class="component">
  <h2>Create Poll</h2>
  <input placeholder="Poll Question" bind:value={question} />

  {#each options as opt, i}
    <input placeholder={`Option ${i+1}`} bind:value={options[i]} />
  {/each}

  <button on:click={addOption}>+ Add Option</button>
  <button on:click={createPoll}>Create Poll</button>
</div>

