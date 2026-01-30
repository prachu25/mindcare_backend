document.addEventListener("DOMContentLoaded", () => {

  // Get logged-in user
  const user = JSON.parse(localStorage.getItem("user"));

  if (!user || !user.id) {
    alert("Please login first");
    window.location.href = "login.html";
    return;
  }

  //  Load existing profile (EDIT MODE)
  fetch(`/user/profile/${user.id}`)
    .then(res => {
      if (!res.ok) throw new Error("Profile not found");
      return res.json();
    })
    .then(data => {
      document.getElementById("age").value = data.age || "";
      document.getElementById("gender").value = data.gender || "";
      document.getElementById("occupation").value = data.occupation || "";
      document.getElementById("sleepHours").value = data.sleepHours || "";
      document.getElementById("stressLevel").value = data.stressLevel || "";
    })
    .catch(() => {
      console.log("New user – no profile yet");
    });
});

/* SAVE PROFILE */
document.getElementById("profileForm").addEventListener("submit", function (e) {
  e.preventDefault();

  const user = JSON.parse(localStorage.getItem("user"));
  if (!user || !user.id) {
    alert("User not logged in");
    return;
  }

  const payload = {
    userId: user.id,
    age: Number(document.getElementById("age").value),
    gender: document.getElementById("gender").value,
    occupation: document.getElementById("occupation").value,
    sleepHours: Number(document.getElementById("sleepHours").value),
    stressLevel: document.getElementById("stressLevel").value
  };

  console.log("SAVING PROFILE:", payload);

  fetch("/user/profile", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(payload)
  })
    .then(res => {
      if (!res.ok) throw new Error("Save failed");
      return res.text();
    })
    .then(() => {
      // Show success message
      document.getElementById("profileForm").style.display = "none";
      document.getElementById("successMessage").style.display = "block";
    })
    .catch(err => {
      console.error(err);
      alert("Failed to save profile ");
    });
});
