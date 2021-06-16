
import AgentsAdmin from '@/components/AgentsAdmin'
import Login from '@/components/Login'
import LotteriesAdmin from '@/components/LotteriesAdmin'
import LotteryRoundsAdmin from '@/components/LotteryRoundsAdmin'
import LotteryRoundsTagsAdmin from '@/components/LotteryRoundTagsAdmin'

const routers = [
    {
      path: '/xxx',
      name: 'Login',
      component: Login
    },
    {
      path: '/agents',
      name: 'Agents',
      component: AgentsAdmin
    },
    {
      path: '/lotteries',
      name: 'Lotteries',
      component: LotteriesAdmin
    },
    {
      path: '/rounds',
      name: 'LotteryRounds',
      component: LotteryRoundsAdmin
    },
    {
      path: '/tags',
      name: 'LotteryRoundsTagsAdmin',
      component: LotteryRoundsTagsAdmin
    }
]
export default routers