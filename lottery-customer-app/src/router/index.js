import Login from '@/components/Login'
import LotteryRounds from '@/components/LotteryRounds'
import LotteryRound from '@/components/LotteryRound'
import Admin from '@/components/Admin'
import Register from '@/components/Register'
import Bids from '@/components/Bids'

const routers = [
    {
      path: '/',
      name: 'Login',
      component: Login
    },
    {
      path: '/register',
      name: 'Register',
      component: Register
    },
    {
      path: '/lottery-rounds',
      name: 'LotteryRounds',
      component: LotteryRounds
    },
    {
      path: '/bids',
      name: 'Bids',
      component: Bids
    },
    {
      path: '/lottery-round',
      name: 'LotteryRound',
      component: LotteryRound
    },
    {
      path: '/admin',
      name: 'Admin',
      component: Admin
    }
]
export default routers