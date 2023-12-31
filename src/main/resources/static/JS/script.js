function togglesidebar(){
	
	const sidebar= document.getElementsByClassName("sidebar")[0];
	const content= document.getElementsByClassName("content")[0];
	
	if(window.getComputedStyle(sidebar).display==="none"){
		sidebar.style.display="block";
		content.style.marginLeft="20%";
	}
	 else{
        sidebar.style.display = "none";
        content.style.marginLeft = "0%";
    }
}

const search=()=>{
	
	let query= $("#search-input").val();
	
	if(query==""){
		$(".search-result").hide();
	}
	else{
		
		let url= `http://localhost:8080/search/${query}`;
		
		fetch(url).then((response)=>{
			return response.json();
		}).then((data)=>{
			
			let text= `<div class='list-group'>`;
			data.forEach((contact)=>{
				text+=`<a href='/user/${contact.cId}/contact' class='list-group-item list-group-action'> ${contact.name}</a>`;
			});
			text+= `</div>`
			$(".search-result").html(text);
			$(".search-result").show();
		});
		
		
	}
}