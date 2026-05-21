import './App.css'
import Home from './pages/home'
import { StompSessionProvider } from "react-stomp-hooks";

function App() {

  return (
    <StompSessionProvider
      url={"ws://localhost:8080/chat"}>
      <Home/>
    </StompSessionProvider>
  )
}

export default App
