#include "plasma-tutorial1.h"
#include <QPainter>
#include <QFontMetrics>
#include <QSizeF>
 
#include <plasma/svg.h>
#include <plasma/theme.h>
 
PlasmaTutorial1::PlasmaTutorial1(QObject *parent, const QVariantList &args)
    : Plasma::Applet(parent, args),
    m_svg(this),
    m_icon("document")
{
    m_svg.setImagePath("widgets/background");
    // this will get us the standard applet background, for free!
    setBackgroundHints(DefaultBackground);
    resize(200, 200);
}
 
 
PlasmaTutorial1::~PlasmaTutorial1()
{
    if (hasFailedToLaunch()) {
        // Do some cleanup here
    } else {
        // Save settings
    }
}
 
void PlasmaTutorial1::init()
{
 
    // A small demonstration of the setFailedToLaunch function
    if (m_icon.isNull()) {
        setFailedToLaunch(true, "No world to say hello");
    }
} 
 
void PlasmaTutorial1::paintInterface(QPainter *p,
        const QStyleOptionGraphicsItem *option, const QRect &contentsRect)
{
    p->setRenderHint(QPainter::SmoothPixmapTransform);
    p->setRenderHint(QPainter::Antialiasing);
 
    // Now we draw the applet, starting with our svg
    m_svg.resize((int)contentsRect.width(), (int)contentsRect.height());
    m_svg.paint(p, (int)contentsRect.left(), (int)contentsRect.top());
 
    // We place the icon and text
    p->drawPixmap(7, 0, m_icon.pixmap((int)contentsRect.width(),(int)contentsRect.width()-14));
    p->save();
    p->setPen(Qt::white);
    p->drawText(contentsRect,
                Qt::AlignBottom | Qt::AlignHCenter,
                "Hello Plasmoid!");
    p->restore();
}
 
#include "plasma-tutorial1.moc"
