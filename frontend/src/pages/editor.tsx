import { useState } from "react";
import { useSubscription } from "react-stomp-hooks";

const Editor = () => {
  // const [analysis, setAnalysis] = useState<string[]>(["No message received yet"]);
  const [analysis, setAnalysis] = useState("");


  useSubscription("/topic/analysis", (message) => {
    console.log("Received analysis:", JSON.parse(message.body));
    // setAnalysis((prevAnalysis) => [...prevAnalysis, analysis]);
    setAnalysis(JSON.parse(message.body));
  })


  return (
    <div>{analysis?.use_cases?.map(uc => (
      <div key={uc.id}>
        <div>
          {uc.actor}</div>
        <div>
          {uc.precondition}</div>
        <div>
          {uc.trigger}
        </div>
        <div>
          {uc.main_flow}
        </div>
      </div>
    ))}</div>
  )
}

export default Editor